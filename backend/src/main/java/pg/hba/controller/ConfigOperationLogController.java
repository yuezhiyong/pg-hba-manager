package pg.hba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pg.hba.config.BusinessException;
import pg.hba.entity.ConfigOperationLog;
import pg.hba.service.ConfigOperationLogService;
import pg.hba.vo.ApiResponse;
import pg.hba.vo.SearchConfigOperationLogRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config-logs")
public class ConfigOperationLogController {


    @Autowired
    private ConfigOperationLogService configOperationLogService;

    /**
     * 获取最新的配置操作日志
     */
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse> getLatestConfigLogs(@RequestParam(defaultValue = "50") int limit) {
        try {
            List<ConfigOperationLog> logs = configOperationLogService.getLatestConfigLogs(limit);
            return ResponseEntity.ok(new ApiResponse(true, "获取最新配置操作日志成功", logs));
        } catch (Exception e) {
            throw new BusinessException("获取配置操作日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 搜索配置操作日志
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse> searchConfigLogs(@RequestBody SearchConfigOperationLogRequest request) {
        try {
            List<ConfigOperationLog> logs = configOperationLogService.searchConfigLogs(
                    request.getKeyword(),
                    request.getOperationType(),
                    request.getStatus(),
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getPage(),
                    request.getSize()
            );

            int total = configOperationLogService.countSearchConfigLogs(
                    request.getKeyword(),
                    request.getOperationType(),
                    request.getStatus(),
                    request.getStartTime(),
                    request.getEndTime()
            );

            Map<String, Object> result = new HashMap<>();
            result.put("logs", logs);
            result.put("total", total);
            result.put("page", request.getPage());
            result.put("size", request.getSize());
            return ResponseEntity.ok(new ApiResponse(true, "搜索配置操作日志成功", result));
        } catch (Exception e) {
            throw new BusinessException("搜索配置操作日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取配置操作统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse> getConfigStatistics() {
        try {
            Map<String, Object> statistics = configOperationLogService.getConfigOperationStatistics();
            return ResponseEntity.ok(new ApiResponse(true, "获取配置统计信息成功", statistics));
        } catch (Exception e) {
            throw new BusinessException("获取配置统计信息失败: " + e.getMessage(), e);
        }
    }
}
