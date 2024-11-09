package liga.tasks.ru.learn.services.exceptions;

public class AuthorNotFoundException extends RuntimeException{

    public AuthorNotFoundException(Long id) {
        super("Не найден автор с id: " + id);
    }
}
