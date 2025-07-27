package pg.hba.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pg.hba.entity.ConfigOperationLog;
import pg.hba.entity.OperationTypeCount;
import pg.hba.entity.StatusCount;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ConfigOperationLogMapper {

    int insert(ConfigOperationLog log);

    List<ConfigOperationLog> findLatest(@Param("limit") int limit);

    List<ConfigOperationLog> findByOperationType(@Param("operationType") String operationType);

    List<ConfigOperationLog> findByUserName(@Param("userName") String userName);

    List<ConfigOperationLog> findByStatus(@Param("status") String status);

    List<ConfigOperationLog> findByDateRange(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);


    List<ConfigOperationLog> search(@Param("keyword") String keyword,
                                    @Param("operationType") String operationType,
                                    @Param("status") String status,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("limit") int limit,
                                    @Param("offset") int offset);

    int countSearch(@Param("keyword") String keyword,
                    @Param("operationType") String operationType,
                    @Param("status") String status,
                    @Param("startTime") LocalDateTime startTime,
                    @Param("endTime") LocalDateTime endTime);

    List<OperationTypeCount> getOperationTypeStatistics();

    List<StatusCount> getStatusStatistics();
}
