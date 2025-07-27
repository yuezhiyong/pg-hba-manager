package pg.hba.vo;

import lombok.Getter;
import lombok.Setter;
import pg.hba.entity.HbaRule;

@Getter
@Setter
public class ParseAndSaveResult {
    private boolean success;
    private HbaRule rule;
    private String message;

    public ParseAndSaveResult(boolean success, HbaRule rule, String message) {
        this.success = success;
        this.rule = rule;
        this.message = message;
    }


}
