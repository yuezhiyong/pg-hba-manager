package pg.hba.vo;

import lombok.Data;

@Data
public class SyncResult {

    private boolean success;
    private String message;
    private int ruleCount;
}
