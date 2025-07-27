package pg.hba.vo;

import lombok.Data;

@Data
public class ReloadResult {
    private boolean success;
    private String message;
    private String methodUsed;
    private String output;
    private String error;
}
