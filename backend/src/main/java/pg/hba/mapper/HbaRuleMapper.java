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

    @Insert("INSERT INTO hba_rules (connection_type, database_name, user_name, address, auth_method, comment, active, created_at, updated_at) " + "VALUES (#{connectionType}, #{databaseName}, #{userName}, #{address}, #{authMethod}, #{comment}, #{active}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(HbaRule rule);


    @Update("UPDATE hba_rules SET connection_type = #{connectionType}, database_name = #{databaseName}, " + "user_name = #{userName}, address = #{address}, auth_method = #{authMethod}, " + "comment = #{comment}, active = #{active}, updated_at = #{updatedAt} " + "WHERE id = #{id}")
    int update(HbaRule rule);

    @Delete("DELETE FROM hba_rules WHERE id = #{id}")
    int deleteById(Long id);

    @Update("UPDATE hba_rules SET active = false, updated_at = #{updatedAt} WHERE id = #{id}")
    int deactivateById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt);


    @Update("UPDATE hba_rules SET active = true, updated_at = #{updatedAt} WHERE id = #{id}")
    int activateById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt);
}
