package pg.hba.mapper;

import org.apache.ibatis.annotations.*;
import pg.hba.entity.HbaRule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface HbaRuleMapper {

    @Select("SELECT * FROM hba_rules WHERE active = true ORDER BY id ASC")
    List<HbaRule> findAllActiveRules();

    @Select("SELECT * FROM hba_rules")
    List<HbaRule> findAllRules();

    @Select("SELECT * FROM hba_rules WHERE active = false ORDER BY id ASC")
    List<HbaRule> findAllInactiveRules();

    @Select("SELECT * FROM hba_rules WHERE id = #{id}")
    Optional<HbaRule> findById(Long id);

    @Select("SELECT * FROM hba_rules WHERE connection_type = #{type} AND database_name = #{database} AND user_name = #{user} AND active = true")
    Optional<HbaRule> findByRule(@Param("type") String type, @Param("database") String database, @Param("user") String user);

    @Select("SELECT * FROM hba_rules WHERE connection_type = #{connectionType} AND active = true")
    List<HbaRule> findByConnectionType(String connectionType);

    @Select("SELECT * FROM hba_rules WHERE database_name = #{databaseName} AND active = true")
    List<HbaRule> findByDatabaseName(String databaseName);

    @Select("SELECT * FROM hba_rules WHERE user_name = #{userName} AND active = true")
    List<HbaRule> findByUserName(String userName);

    int insertRule(HbaRule rule);


    int update(HbaRule rule);

    @Delete("DELETE FROM hba_rules WHERE id = #{id}")
    int deleteById(Long id);

    @Update("UPDATE hba_rules SET active = false, updated_at = #{updatedAt} WHERE id = #{id}")
    int deactivateById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt);


    @Update("UPDATE hba_rules SET active = true, updated_at = #{updatedAt} WHERE id = #{id}")
    int activateById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt);
}
