package pg.hba.vo;

import lombok.Data;

@Data
public class PermissionResult {

    private boolean hasPermission;
    private String message;
}
