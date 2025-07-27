package pg.hba.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLog {
    private Long id;
    private Long ruleId;
    private String action; // CREATE, UPDATE, DELETE, ACTIVATE, DEACTIVATE
    private String userName;
    private String ipAddress;
    private String userAgent;
    private String oldValue;
    private String newValue;
    private String remarks;
    private LocalDateTime createdAt;
}
