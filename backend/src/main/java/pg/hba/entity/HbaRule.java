package pg.hba.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HbaRule {

    private Long id;

    private String connectionType;

    private String databaseName;

    private String userName;

    private String address; // 对于local类型可以为null

    private String authMethod;

    private String comment;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}