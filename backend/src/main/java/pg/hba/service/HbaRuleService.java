package pg.hba.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pg.hba.config.BusinessException;
import pg.hba.config.ResourceNotFoundException;
import pg.hba.entity.HbaRule;
import pg.hba.mapper.HbaRuleMapper;
import pg.hba.parser.HbaRuleParser;
import pg.hba.vo.ParseAndSaveResult;
import pg.hba.vo.ParseResult;
import pg.hba.vo.RuleAction;
import pg.hba.vo.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
public class HbaRuleService {

    @Autowired
    private HbaRuleMapper hbaRuleMapper;

    @Autowired
    private HbaRuleParser hbaRuleParser;

    @Autowired
    private RuleValidationService validationService;

    @Autowired
    private HbaConfigService hbaConfigService;


    @Autowired
    private AuditLogService auditLogService;


    @Autowired
    private DatabaseConnectionService databaseConnectionService;


    /**
     * 检查 PostgreSQL 数据库连接状态
     */
    public boolean checkPostGreSQLConnection() {
        try {
            return databaseConnectionService.checkPostgreSQLStatus();
        } catch (Exception e) {
            log.error("Failed to check PostGreSQL connection: {}", e.getMessage(), e);
            return false;
        }
    }


    /**
     * 获取 PostgreSQL 数据库版本
     */
    public String getPostGreSQLVersion() {
        try {
            return databaseConnectionService.getPostGreSQLVersion();
        } catch (Exception e) {
            log.error("Failed to get PostGreSQL version: {}", e.getMessage(), e);
            return "Unknown";
        }
    }


    public ParseAndSaveResult parseAndSaveRule(String ruleLine, HttpServletRequest httpServletRequest) {
        // 解析规则
        ParseResult parseResult = hbaRuleParser.parseRule(ruleLine);
        if (!parseResult.isSuccess()) {
            return new ParseAndSaveResult(false, null, "Parse failed: " + parseResult.getErrorMessage());
        }
        HbaRule rule = parseResult.getRule();
        // 验证规则
        ValidationResult validationResult = validationService.validateRule(rule);
        if (!validationResult.isValid()) {
            return new ParseAndSaveResult(false, rule, "Validation failed: " + String.join(", ", validationResult.getErrors()));
        }
        // 检查重复规则
        Optional<HbaRule> existingRule = hbaRuleMapper.findByRule(rule.getConnectionType(), rule.getDatabaseName(), rule.getUserName());

        if (existingRule.isPresent()) {
            return new ParseAndSaveResult(false, rule, "Rule already exists with same connection type, database, and user");
        }
        // 保存规则
        rule.setCreatedAt(LocalDateTime.now());
        rule.setActive(false);
        int res = hbaRuleMapper.insertRule(rule);
        if (res < 1) {
            throw new BusinessException("保存失败");
        }
        auditLogService.logAudit(rule.getId(), RuleAction.CREATE, null, null, rule, "用户创建规则", httpServletRequest);
        return new ParseAndSaveResult(true, hbaRuleMapper.findById(rule.getId()).orElse(null), "Rule saved successfully");
    }

    @Transactional(readOnly = true)
    public List<HbaRule> getAllActiveRules() {
        return hbaRuleMapper.findAllActiveRules();
    }


    @Transactional(readOnly = true)
    public List<HbaRule> getAllRules() {
        return hbaRuleMapper.findAllRules();
    }


    @Transactional(readOnly = true)
    public List<HbaRule> getAllInactiveRules() {
        return hbaRuleMapper.findAllInactiveRules();
    }


    @Transactional(readOnly = true)
    public Optional<HbaRule> getRuleById(Long id) {
        return hbaRuleMapper.findById(id);
    }

    public HbaRule updateRule(Long id, HbaRule rule, HttpServletRequest servletRequest) {
        Optional<HbaRule> existingRule = hbaRuleMapper.findById(id);
        if (existingRule.isPresent()) {
            HbaRule ruleToUpdate = existingRule.get();
            ruleToUpdate.setConnectionType(rule.getConnectionType());
            ruleToUpdate.setDatabaseName(rule.getDatabaseName());
            ruleToUpdate.setUserName(rule.getUserName());
            ruleToUpdate.setAddress(rule.getAddress());
            ruleToUpdate.setAuthMethod(rule.getAuthMethod());
            ruleToUpdate.setComment(rule.getComment());
            ruleToUpdate.setActive(rule.getActive());
            ruleToUpdate.setUpdatedAt(LocalDateTime.now());
            int res = hbaRuleMapper.update(ruleToUpdate);
            if (res < 1) {
                throw new BusinessException("更新失败");
            }
            HbaRule updated = hbaRuleMapper.findById(ruleToUpdate.getId()).orElse(null);
            auditLogService.logAudit(id, RuleAction.UPDATE, null, rule, updated, "用户更新规则", servletRequest);
            return updated;
        }
        throw new RuntimeException("Rule not found with id: " + id);
    }

    public void deleteRule(Long id, HttpServletRequest httpServletRequest) {
        final HbaRule hbaRule = getRuleById(id).orElse(null);
        if (hbaRule == null) {
            throw new ResourceNotFoundException("规则不存在");
        }
        hbaRuleMapper.deleteById(id);
        auditLogService.logAudit(id, RuleAction.DELETE, null, hbaRule, null, "用户删除规则", httpServletRequest);
    }

    public void deactivateRule(Long id, HttpServletRequest httpServletRequest) {
        final HbaRule hbaRule = getRuleById(id).orElse(null);
        if (hbaRule == null) {
            throw new ResourceNotFoundException("规则不存在");
        }
        if (!hbaRule.getActive()) {
            throw new BusinessException("规则已经停用");
        }
        int res = hbaRuleMapper.deactivateById(id, LocalDateTime.now());
        if (res < 1) {
            throw new BusinessException("激活失败");
        }
        hbaConfigService.syncRulesToConfig(Collections.singletonList(hbaRule), httpServletRequest);
        HbaRule newRule = new HbaRule();
        BeanUtils.copyProperties(hbaRule, newRule);
        newRule.setActive(false);
        auditLogService.logAudit(id, RuleAction.DEACTIVATE, null, hbaRule, newRule, "用户激活规则", httpServletRequest);
    }


    public void activateRule(Long id, HttpServletRequest httpServletRequest) {
        final HbaRule hbaRule = getRuleById(id).orElse(null);
        if (hbaRule == null) {
            throw new BusinessException("规则不存在");
        }
        if (hbaRule.getActive()) {
            throw new BusinessException("规则已经激活");
        }
        ValidationResult result = validationService.validateRule(hbaRule);
        if (!result.isValid()) {
            throw new BusinessException(String.join(",", result.getErrors()));
        }
        int res = hbaRuleMapper.activateById(id, LocalDateTime.now());
        if (res < 1) {
            throw new BusinessException("激活规则失败");
        }
        HbaRule newRule = new HbaRule();
        BeanUtils.copyProperties(hbaRule, newRule);
        hbaConfigService.syncRulesToConfig(Collections.singletonList(hbaRule), httpServletRequest);
        auditLogService.logAudit(id, RuleAction.ACTIVATE, null, hbaRule, newRule, "用户激活规则", httpServletRequest);

    }


}
