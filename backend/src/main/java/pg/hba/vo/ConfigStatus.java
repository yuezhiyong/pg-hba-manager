package pg.hba.vo;

import lombok.Data;

@Data
public class ConfigStatus {

    private String path;
    private boolean exists;
    private boolean writable;
    private String lastModified;
    private long size;
}
