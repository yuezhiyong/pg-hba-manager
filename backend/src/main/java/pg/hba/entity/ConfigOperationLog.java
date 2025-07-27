package pg.hba.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigOperationLog {

    private Long id;
    private String operationType; // SYNC_CONFIG, RELOAD_CONFIG, CHECK_PERMISSION
    private String userName;
    private String ipAddress;
    private String userAgent;
    private String status; // SUCCESS, FAILED
    private String message;
    private String details; // JSON格式的详细信息
    private Long durationMs; // 操作耗时（毫秒）
    private LocalDateTime createdAt;

    // Constructors
    public ConfigOperationLog() {
    }

    public ConfigOperationLog(String operationType, String userName, String ipAddress,
                              String userAgent, String status, String message,
                              String details, Long durationMs) {
        this.operationType = operationType;
        this.userName = userName;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.status = status;
        this.message = message;
        this.details = details;
        this.durationMs = durationMs;
        this.createdAt = LocalDateTime.now();
    }

}
