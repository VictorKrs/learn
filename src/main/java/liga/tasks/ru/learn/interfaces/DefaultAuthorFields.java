package liga.tasks.ru.learn.interfaces;

import java.util.List;

import liga.tasks.ru.learn.models.book.BookWithoutAuthors;

public interface DefaultAuthorFields {
    String getName();
    List<BookWithoutAuthors> getBooks();
}
