package pg.hba.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pg.hba.vo.ApiError;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExHandler.class);


    // 处理自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex, WebRequest request) {
        logger.warn("Business exception occurred: {}", ex.getMessage(), ex);

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    // 处理参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation exception occurred: {}", ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed", request.getDescription(false));
        apiError.setDetails(errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    // 处理数据库访问异常
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(DataAccessException ex, WebRequest request) {
        logger.error("Database access error occurred: {}", ex.getMessage(), ex);

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Database operation failed", request.getDescription(false));

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // 处理SQL异常
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleSQLException(SQLException ex, WebRequest request) {
        logger.error("SQL error occurred: {}", ex.getMessage(), ex);

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Database operation failed: " + ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // 处理资源未找到异常
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }


    // 处理IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Illegal argument: {}", ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // 处理通用异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request.getDescription(false));

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

