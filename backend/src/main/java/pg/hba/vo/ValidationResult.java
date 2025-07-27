package pg.hba.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationResult {

    private boolean valid;
    private final List<String> errors = new ArrayList<>();


    public void addError(String errorMsg) {
        errors.add(errorMsg);
    }

}
