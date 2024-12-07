package liga.tasks.ru.learn.interfaces;

import java.util.List;

import liga.tasks.ru.learn.models.author.AuthorWithoutBooks;

public interface DefaultBookFields {
    String getTitle();
    List<AuthorWithoutBooks> getAuthors();
}
