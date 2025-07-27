package pg.hba.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pg.hba.entity.AuditLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface AuditLogMapper {


    int insert(AuditLog auditLog);

    List<AuditLog> findLatest(@Param("limit") int limit);

    List<AuditLog> findByRuleId(@Param("ruleId") Long ruleId);

    List<AuditLog> findByAction(@Param("action") String action);

    List<AuditLog> findByUserName(@Param("userName") String userName);

    List<AuditLog> findByDateRange(@Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);


    List<AuditLog> search(@Param("keyword") String keyword,
                          @Param("action") String action,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime,
                          @Param("limit") int limit,
                          @Param("offset") int offset);


    int countSearch(@Param("keyword") String keyword,
                    @Param("action") String action,
                    @Param("startTime") LocalDateTime startTime,
                    @Param("endTime") LocalDateTime endTime);

    int deleteBefore(@Param("beforeTime") LocalDateTime beforeTime);


    @Select("SELECT action, COUNT(*) as count FROM audit_logs GROUP BY action")
    List<Map<String, Object>> getActionStatistics();

    @Select("SELECT user_name, COUNT(*) as count FROM audit_logs GROUP BY user_name ORDER BY count DESC LIMIT 10")
    List<Map<String, Object>> getUserStatistics();


    @Select("SELECT DATE(created_at) as date, COUNT(*) as count FROM audit_logs " +
            "WHERE created_at >= #{startDate} " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY date")
    List<Map<String, Object>> getDailyStatistics(@Param("startDate") LocalDateTime startDate);
}
