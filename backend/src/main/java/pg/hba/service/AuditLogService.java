package pg.hba.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.hba.entity.AuditLog;
import pg.hba.mapper.AuditLogMapper;
import pg.hba.vo.RuleAction;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuditLogService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 记录审计日志
     */
    public void logAudit(Long ruleId, RuleAction action,
                         String userName,
                         Object oldValue, Object newValue, String remarks,
                         HttpServletRequest request) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setRuleId(ruleId);
            auditLog.setAction(action.name());
            auditLog.setUserName(userName != null ? userName : "anonymous");
            auditLog.setIpAddress(getClientIpAddress(request));
            auditLog.setUserAgent(request != null ? request.getHeader("User-Agent") : "");
            auditLog.setOldValue(objectToJson(oldValue));
            auditLog.setNewValue(objectToJson(newValue));
            auditLog.setRemarks(remarks);
            auditLog.setCreatedAt(LocalDateTime.now());
            auditLogMapper.insert(auditLog);
        } catch (Exception e) {
            // 记录审计日志不应该影响主业务流程
            log.error("记录操作审计日志出现错误:", e);
        }
    }

    /**
     * 获取最新审计日志
     */
    public List<AuditLog> getLatestAuditLogs(int limit) {
        return auditLogMapper.findLatest(limit);
    }

    /**
     * 根据规则ID获取审计日志
     */
    public List<AuditLog> getAuditLogsByRuleId(Long ruleId) {
        return auditLogMapper.findByRuleId(ruleId);
    }

    /**
     * 根据操作类型获取审计日志
     */
    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditLogMapper.findByAction(action);
    }

    /**
     * 搜索审计日志
     */
    public List<AuditLog> searchAuditLogs(String keyword, String action,
                                          LocalDateTime startTime, LocalDateTime endTime,
                                          int page, int size) {
        int offset = (page - 1) * size;
        return auditLogMapper.search(keyword, action, startTime, endTime, size, offset);
    }

    /**
     * 获取搜索结果总数
     */
    public int countSearchAuditLogs(String keyword, String action,
                                    LocalDateTime startTime, LocalDateTime endTime) {
        return auditLogMapper.countSearch(keyword, action, startTime, endTime);
    }

    /**
     * 清理旧的审计日志
     */
    public int cleanupOldLogs(int daysToKeep) {
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(daysToKeep);
        return auditLogMapper.deleteBefore(beforeTime);
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


    /**
     * 获取审计日志统计信息
     */
    public Map<String, Object> getAuditStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            // 获取总日志数
            int totalLogs = auditLogMapper.countSearch(null, null, null, null);
            statistics.put("totalLogs", totalLogs);

            // 获取今日日志数
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
            int todayLogs = auditLogMapper.countSearch(null, null, todayStart, todayEnd);
            statistics.put("todayLogs", todayLogs);

            // 获取各种操作类型的统计
            int createCount = auditLogMapper.countSearch(null, "CREATE", null, null);
            int updateCount = auditLogMapper.countSearch(null, "UPDATE", null, null);
            int deleteCount = auditLogMapper.countSearch(null, "DELETE", null, null);
            int activateCount = auditLogMapper.countSearch(null, "ACTIVATE", null, null);
            int deactivateCount = auditLogMapper.countSearch(null, "DEACTIVATE", null, null);

            statistics.put("createCount", createCount);
            statistics.put("updateCount", updateCount);
            statistics.put("deleteCount", deleteCount);
            statistics.put("activateCount", activateCount);
            statistics.put("deactivateCount", deactivateCount);

            // 获取最近7天的日志趋势
            List<Map<String, Object>> trendData = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                LocalDateTime dayStart = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime dayEnd = LocalDateTime.now().minusDays(i).withHour(23).withMinute(59).withSecond(59);
                int dayCount = auditLogMapper.countSearch(null, null, dayStart, dayEnd);

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", dayStart.toLocalDate().toString());
                dayData.put("count", dayCount);
                trendData.add(dayData);
            }
            statistics.put("trendData", trendData);

            // 获取活跃用户统计（最近30天）
            List<Map<String, Object>> userStats = getActiveUsersStatistics();
            statistics.put("userStats", userStats);

        } catch (Exception e) {
            log.error("获取审计统计信息失败: {}", e.getMessage(), e);
            // 返回默认值避免页面出错
            statistics.put("totalLogs", 0);
            statistics.put("todayLogs", 0);
            statistics.put("createCount", 0);
            statistics.put("updateCount", 0);
            statistics.put("deleteCount", 0);
            statistics.put("activateCount", 0);
            statistics.put("deactivateCount", 0);
        }

        return statistics;
    }


    /**
     * 获取活跃用户统计
     */
    private List<Map<String, Object>> getActiveUsersStatistics() {
        List<Map<String, Object>> userStats = new ArrayList<>();
        try {
            // 这里可以实现更复杂的用户统计逻辑
            // 暂时返回示例数据
            Map<String, Object> adminStats = new HashMap<>();
            adminStats.put("userName", "admin");
            adminStats.put("operationCount", 150);
            userStats.add(adminStats);
            Map<String, Object> userStats2 = new HashMap<>();
            userStats2.put("userName", "operator");
            userStats2.put("operationCount", 85);
            userStats.add(userStats2);
        } catch (Exception e) {
            log.warn("获取用户统计信息失败: {}", e.getMessage());
        }
        return userStats;
    }


    /**
     * 获取最近N天的每日操作统计
     */
    public List<Map<String, Object>> getDailyStatistics(int days) {
        List<Map<String, Object>> statistics = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime dayStart = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime dayEnd = LocalDateTime.now().minusDays(i).withHour(23).withMinute(59).withSecond(59);
            int count = auditLogMapper.countSearch(null, null, dayStart, dayEnd);
            Map<String, Object> dayStat = new HashMap<>();
            dayStat.put("date", dayStart.toLocalDate().toString());
            dayStat.put("count", count);
            statistics.add(dayStat);
        }
        return statistics;
    }

    /**
     * 获取用户操作统计
     */
    public List<Map<String, Object>> getUserOperationStatistics() {
        // 这里可以通过自定义SQL查询实现
        // 暂时返回示例数据
        List<Map<String, Object>> userStats = new ArrayList<>();

        Map<String, Object> user1 = new HashMap<>();
        user1.put("userName", "admin");
        user1.put("count", 150);
        userStats.add(user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("userName", "operator");
        user2.put("count", 85);
        userStats.add(user2);

        return userStats;
    }

}
