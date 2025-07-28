package pg.hba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pg.hba.config.BusinessException;
import pg.hba.entity.HbaRule;
import pg.hba.service.HbaConfigService;
import pg.hba.service.HbaRuleService;
import pg.hba.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hba-rules")
@CrossOrigin(origins = "*")
@Slf4j
public class HbaRuleController {

    @Autowired
    private HbaRuleService hbaRuleService;


    @Autowired
    private HbaConfigService hbaConfigService;


    @PostMapping("/parse-and-save")
    public ResponseEntity<?> parseAndSaveRule(@RequestBody RuleRequest request, HttpServletRequest httpServletRequest) {
        try {
            ParseAndSaveResult result = hbaRuleService.parseAndSaveRule(request.getRuleLine(), httpServletRequest);
            if (result.isSuccess()) {
                return ResponseEntity.ok(new ApiResponse(true, result.getMessage(), result.getRule()));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse(false, result.getMessage(), result.getRule()));
            }
        } catch (Exception e) {
            log.error("保存出现异常:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<List<HbaRule>> getAllActiveRules() {
        return ResponseEntity.ok(hbaRuleService.getAllActiveRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRuleById(@PathVariable Long id) {
        Optional<HbaRule> rule = hbaRuleService.getRuleById(id);
        if (rule.isPresent()) {
            return ResponseEntity.ok(rule.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/active")
    public ResponseEntity<?> allActiveRules() {
        return ResponseEntity.ok(hbaRuleService.getAllActiveRules());
    }

    @GetMapping("/inactive")
    public ResponseEntity<?> allInactiveRules() {
        return ResponseEntity.ok(hbaRuleService.getAllInactiveRules());
    }


    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Long id, HttpServletRequest servletRequest) {
        try {
            hbaRuleService.activateRule(id, servletRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Rule deactivated successfully", null));
        } catch (Exception e) {
            log.error("激活规则失败:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to active rule: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> allRules() {
        return ResponseEntity.ok(hbaRuleService.getAllRules());
    }


    @GetMapping("/config-status")
    public ResponseEntity<ApiResponse> getConfigStatus() {
        try {
            ConfigStatus status = hbaConfigService.getConfigStatus();
            return ResponseEntity.ok(new ApiResponse(true, "Configuration status retrieved", status));
        } catch (Exception e) {
            throw new BusinessException("Failed to get configuration status: " + e.getMessage(), e);
        }
    }


    @GetMapping("/check-permission")
    public ResponseEntity<ApiResponse> checkWritePermission(HttpServletRequest servletRequest) {
        try {
            // 调用 Service 层方法检查权限
            PermissionResult hasPermission = hbaConfigService.checkWritePermission(servletRequest);
            // 根据检查结果构造响应
            String message = hasPermission.isHasPermission() ? "Write permission granted" : "Write permission denied";
            return ResponseEntity.ok(new ApiResponse(true, message, hasPermission));
        } catch (Exception e) {
            // 如果检查过程中发生异常，则抛出业务异常
            throw new BusinessException("Failed to check permissions: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRule(@PathVariable Long id, @RequestBody HbaRule rule, HttpServletRequest servletRequest) {
        try {
            HbaRule updatedRule = hbaRuleService.updateRule(id, rule, servletRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Rule updated successfully", updatedRule));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRule(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        try {
            hbaRuleService.deleteRule(id, httpServletRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Rule deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to delete rule: " + e.getMessage(), null));
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateRule(@PathVariable Long id, HttpServletRequest servletRequest) {
        try {
            hbaRuleService.deactivateRule(id, servletRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Rule deactivated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to deactivate rule: " + e.getMessage(), null));
        }
    }


    @PostMapping("/sync-config")
    public ResponseEntity<ApiResponse> syncToPgHbaConfig(HttpServletRequest servletRequest) {
        try {
            List<HbaRule> rules = hbaRuleService.getAllActiveRules();
            if (rules == null || rules.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(false, "No any active configuration item", null));
            }
            SyncResult syncResult = hbaConfigService.syncRulesToConfig(rules, servletRequest);
            if (syncResult.isSuccess()) {
                return ResponseEntity.ok(new ApiResponse(true, "Configuration synced successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, syncResult.getMessage(), syncResult));
            }
        } catch (Exception e) {
            throw new BusinessException("Failed to sync configuration: " + e.getMessage(), e);
        }
    }

    @PostMapping("/reload-postgresql")
    public ResponseEntity<ApiResponse> reloadPostGreSQL(HttpServletRequest servletRequest) {
        try {
            ReloadResult result = hbaConfigService.reloadPostGreSQLConfig(servletRequest);
            if (result.isSuccess()) {
                return ResponseEntity.ok(new ApiResponse(true, "PostGreSQL configuration reloaded successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, result.getMessage(), result));
            }
        } catch (Exception e) {
            log.error("重载postgresql失败:", e);
            throw new BusinessException("Failed to reload PostGreSQL configuration: " + e.getMessage(), e);
        }
    }


    @GetMapping("/config-content")
    public ResponseEntity<ApiResponse> readPgHbaConfigContent() {
        try {
            String content = hbaConfigService.readPgHbaConfigContent();
            Map<String, Object> data = new HashMap<>();
            data.put("content", content);
            data.put("path", hbaConfigService.getConfiguredPgHbaPath());
            return ResponseEntity.ok(new ApiResponse(true, "Configuration content retrieved successfully", data));
        } catch (Exception e) {
            throw new BusinessException("Failed to read configuration content: " + e.getMessage(), e);
        }
    }

    /**
     * 更新pg_hba.conf文件内容
     */
    @PutMapping("/config-content")
    public ResponseEntity<ApiResponse> updatePgHbaConfigContent(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        try {
            String newContent = request.get("content");
            if (newContent == null || newContent.trim().isEmpty()) {
                throw new BusinessException("Configuration content cannot be empty");
            }

            UpdateConfigResult result =
                    hbaConfigService.updatePgHbaConfigContent(newContent, httpRequest);

            if (result.isSuccess()) {
                return ResponseEntity.ok(new ApiResponse(true, result.getMessage(), result));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, result.getMessage(), result));
            }
        } catch (Exception e) {
            throw new BusinessException("Failed to update configuration content: " + e.getMessage(), e);
        }
    }


    @GetMapping("/postgres-status")
    public ResponseEntity<ApiResponse> checkPostgreSQLStatus() {
        try {
            // 检查 PostGreSQL 连接状态
            boolean connected = hbaRuleService.checkPostGreSQLConnection();
            String version = hbaRuleService.getPostGreSQLVersion();
            Map<String, Object> data = new HashMap<>();
            data.put("connected", connected);
            data.put("version", version);
            String message = connected ? "PostGreSQL is connected" : "PostgreSQL is not connected";
            return ResponseEntity.ok(new ApiResponse(true, message, data));
        } catch (Exception e) {
            throw new BusinessException("Failed to check PostgreSQL status: " + e.getMessage(), e);
        }
    }


}
