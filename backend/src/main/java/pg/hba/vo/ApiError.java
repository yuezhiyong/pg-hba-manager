package pg.hba.vo;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {


    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Object details;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, String path) {
        this();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
