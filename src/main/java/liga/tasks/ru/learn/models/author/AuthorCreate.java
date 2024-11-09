package liga.tasks.ru.learn.models.author;

import java.util.Set;

import liga.tasks.ru.learn.models.book.BookWithoutAuthors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorCreate {

    private String name;
    private Set<BookWithoutAuthors> books;
}
