package pg.hba.vo;

import lombok.Getter;
import lombok.Setter;
import pg.hba.entity.HbaRule;

@Getter
@Setter
public class ParseResult {

    private boolean success;
    private HbaRule rule;
    private String errorMessage;

    public ParseResult(boolean success, HbaRule rule, String errorMessage) {
        this.success = success;
        this.rule = rule;
        this.errorMessage = errorMessage;
    }

}
