package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

public class BaseRuntimeException extends RuntimeException {

    private HttpStatus status;
    
    public BaseRuntimeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }
}
