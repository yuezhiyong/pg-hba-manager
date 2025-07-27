package pg.hba.entity;

import lombok.Data;

@Data
public class OperationTypeCount {

    private String operationType;
    private int count;
}
