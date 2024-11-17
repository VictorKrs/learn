package liga.tasks.ru.learn.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import liga.tasks.ru.learn.exceptions.BaseRuntimeException;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<String> baseRuntimeExceptionHandler(BaseRuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }
}
