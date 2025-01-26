package liga.tasks.ru.learn.exceptions.user;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class UserAlreadyExistException extends BaseRuntimeException {

    public UserAlreadyExistException(String username) {
        super("Пользователь с именем " + username + " уже существует", HttpStatus.BAD_REQUEST);
    }
}
