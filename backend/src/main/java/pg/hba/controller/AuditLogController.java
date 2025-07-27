package pg.hba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pg.hba.config.BusinessException;
import pg.hba.entity.AuditLog;
import pg.hba.service.AuditLogService;
import pg.hba.service.HbaRuleService;
import pg.hba.vo.ApiResponse;
import pg.hba.vo.SearchAuditLogRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "*")
public class AuditLogController {

    @Autowired
    private HbaRuleService hbaRuleService;


    @Autowired
    private AuditLogService auditLogService;

    /**
     * 获取最新的审计日志
     */
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse> getLatestAuditLogs(@RequestParam(defaultValue = "50") int limit) {
        try {
            List<AuditLog> logs = auditLogService.getLatestAuditLogs(limit);
            return ResponseEntity.ok(new ApiResponse(true, "获取最新审计日志成功", logs));
        } catch (Exception e) {
            throw new BusinessException("获取审计日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据规则ID获取审计日志
     */
    @GetMapping("/rule/{ruleId}")
    public ResponseEntity<ApiResponse> getAuditLogsByRuleId(@PathVariable Long ruleId) {
        try {
            List<AuditLog> logs = auditLogService.getAuditLogsByRuleId(ruleId);
            return ResponseEntity.ok(new ApiResponse(true, "获取规则审计日志成功", logs));
        } catch (Exception e) {
            throw new BusinessException("获取规则审计日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 搜索审计日志
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse> searchAuditLogs(@RequestBody SearchAuditLogRequest request) {
        try {
            List<AuditLog> logs = auditLogService.searchAuditLogs(
                    request.getKeyword(),
                    request.getAction(),
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getPage(),
                    request.getSize()
            );

            int total = auditLogService.countSearchAuditLogs(
                    request.getKeyword(),
                    request.getAction(),
                    request.getStartTime(),
                    request.getEndTime()
            );

            Map<String, Object> result = new HashMap<>();
            result.put("logs", logs);
            result.put("total", total);
            result.put("page", request.getPage());
            result.put("size", request.getSize());
            return ResponseEntity.ok(new ApiResponse(true, "搜索审计日志成功", result));
        } catch (Exception e) {
            throw new BusinessException("搜索审计日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取操作类型统计
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse> getAuditStatistics() {
        try {
            // 调用Service获取真实统计数据
            Map<String, Object> statistics = auditLogService.getAuditStatistics();
            return ResponseEntity.ok(new ApiResponse(true, "获取统计信息成功", statistics));
        } catch (Exception e) {
            throw new BusinessException("获取统计信息失败: " + e.getMessage(), e);
        }
    }
}
