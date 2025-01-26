package liga.tasks.ru.learn.controllers;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<String> baseRuntimeExceptionHandler(BaseRuntimeException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        
        return new ResponseEntity<>(exception.getMessage(), headers, exception.getStatus());
    }
}
