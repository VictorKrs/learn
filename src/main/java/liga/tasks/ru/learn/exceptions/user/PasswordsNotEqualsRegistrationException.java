package liga.tasks.ru.learn.exceptions.user;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class PasswordsNotEqualsRegistrationException extends BaseRuntimeException {

    public PasswordsNotEqualsRegistrationException() {
        super("Пароли не совпадают", HttpStatus.BAD_REQUEST);
    }

}
