package pg.hba.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Service
public class DatabaseConnectionService {


    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.username:postgres}")
    private String dbUsername;






    /**
     * 通过SQL命令重载PostgreSQL配置
     *
     * @return 重载是否成功
     */
    public boolean reloadPostGreSQLConfigViaSQL() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            // 检查是否有权限执行pg_reload_conf
            if (!hasReloadPermission(connection)) {
                log.warn("User {} does not have permission to execute pg_reload_conf()", dbUsername);
                return false;
            }
            // 执行配置重载
            statement = connection.prepareStatement("SELECT pg_reload_conf()");
            boolean result = statement.execute();
            if (result) {
                log.info("PostGreSQL configuration reloaded successfully via SQL");
                return true;
            } else {
                log.warn("PostGreSQL configuration reload via SQL returned false");
                return false;
            }
        } catch (SQLException e) {
            log.error("Failed to reload PostgreSQL configuration via SQL: {}", e.getMessage(), e);
            return false;
        } finally {
            // 关闭资源
            closeQuietly(statement);
            closeQuietly(connection);
        }
    }

    /**
     * 检查当前用户是否有权限执行pg_reload_conf
     */
    private boolean hasReloadPermission(Connection connection) {
        PreparedStatement statement = null;
        try {
            // 检查用户是否有执行pg_reload_conf的权限
            statement = connection.prepareStatement(
                    "SELECT has_function_privilege(current_user, 'pg_reload_conf()', 'execute')"
            );

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
            return false;

        } catch (SQLException e) {
            log.warn("Failed to check reload permission: {}", e.getMessage());
            return false;
        } finally {
            closeQuietly(statement);
        }
    }

    /**
     * 检查PostgreSQL服务器状态
     */
    public boolean checkPostgreSQLStatus() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return connection.isValid(5); // 5秒超时
        } catch (SQLException e) {
            log.error("PostgreSQL connection check failed: {}", e.getMessage());
            return false;
        } finally {
            closeQuietly(connection);
        }
    }

    /**
     * 获取PostGreSQL服务器版本信息
     */
    public String getPostGreSQLVersion() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT version()");

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return "Unknown";
        } catch (SQLException e) {
            log.error("Failed to get PostgreSQL version: {}", e.getMessage());
            return "Error: " + e.getMessage();
        } finally {
            closeQuietly(statement);
            closeQuietly(connection);
        }
    }

    /**
     * 安静地关闭数据库连接
     */
    private void closeQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.debug("Failed to close connection: {}", e.getMessage());
            }
        }
    }

    /**
     * 安静地关闭PreparedStatement
     */
    private void closeQuietly(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.debug("Failed to close statement: {}", e.getMessage());
            }
        }
    }
}
