package pg.hba.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.hba.entity.ConfigOperationLog;
import pg.hba.mapper.ConfigOperationLogMapper;
import pg.hba.entity.OperationTypeCount;
import pg.hba.entity.StatusCount;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ConfigOperationLogService {

    @Autowired
    private ConfigOperationLogMapper configOperationLogMapper;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 记录配置操作日志
     */
    public void logOperation(String operationType, String userName, String status,
                             String message, Object details, Long durationMs,
                             HttpServletRequest request) {
        try {
            ConfigOperationLog log = new ConfigOperationLog();
            log.setOperationType(operationType);
            log.setUserName(userName != null ? userName : "anonymous");
            log.setIpAddress(getClientIpAddress(request));
            log.setUserAgent(request != null ? request.getHeader("User-Agent") : "");
            log.setStatus(status);
            log.setMessage(message);
            log.setDetails(objectToJson(details));
            log.setDurationMs(durationMs);
            log.setCreatedAt(LocalDateTime.now());
            configOperationLogMapper.insert(log);
        } catch (Exception e) {
            // 记录日志不应该影响主业务流程
            log.error("记录日志出现错误:", e);
        }
    }

    /**
     * 获取最新配置操作日志
     */
    public List<ConfigOperationLog> getLatestConfigLogs(int limit) {
        return configOperationLogMapper.findLatest(limit);
    }

    /**
     * 搜索配置操作日志
     */
    public List<ConfigOperationLog> searchConfigLogs(String keyword, String operationType,
                                                     String status, LocalDateTime startTime,
                                                     LocalDateTime endTime, int page, int size) {
        int offset = (page - 1) * size;
        return configOperationLogMapper.search(keyword, operationType, status,
                startTime, endTime, size, offset);
    }

    /**
     * 获取搜索结果总数
     */
    public int countSearchConfigLogs(String keyword, String operationType,
                                     String status, LocalDateTime startTime,
                                     LocalDateTime endTime) {
        return configOperationLogMapper.countSearch(keyword, operationType, status,
                startTime, endTime);
    }

    /**
     * 获取配置操作统计信息
     */
    public Map<String, Object> getConfigOperationStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            // 获取总操作数
            int totalLogs = configOperationLogMapper.countSearch(null, null, null, null, null);
            statistics.put("totalLogs", totalLogs);

            // 获取今日操作数
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
            int todayLogs = configOperationLogMapper.countSearch(null, null, null, todayStart, todayEnd);
            statistics.put("todayLogs", todayLogs);

            // 获取各操作类型统计
            List<OperationTypeCount> typeStats =
                    configOperationLogMapper.getOperationTypeStatistics();
            Map<String, Integer> typeStatistics = new HashMap<>();
            for (OperationTypeCount stat : typeStats) {
                typeStatistics.put(stat.getOperationType(), stat.getCount());
            }
            statistics.put("operationTypeStats", typeStatistics);

            // 获取状态统计
            List<StatusCount> statusStats =
                    configOperationLogMapper.getStatusStatistics();
            Map<String, Integer> statusStatistics = new HashMap<>();
            for (StatusCount stat : statusStats) {
                statusStatistics.put(stat.getStatus(), stat.getCount());
            }
            statistics.put("statusStats", statusStatistics);

            // 获取最近7天的趋势数据
            List<Map<String, Object>> trendData = getDailyTrendData(7);
            statistics.put("trendData", trendData);

        } catch (Exception e) {
            log.error("获取操作日志统计信息出现错误:", e);
        }
        return statistics;
    }

    /**
     * 获取每日趋势数据
     */
    private List<Map<String, Object>> getDailyTrendData(int days) {
        // 这里可以实现具体的趋势数据查询逻辑
        // 暂时返回空列表
        return new java.util.ArrayList<>();
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * 将对象转换为JSON字符串
     */
    private String objectToJson(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}
