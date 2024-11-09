package liga.tasks.ru.learn.models.book;

import java.util.Set;

import liga.tasks.ru.learn.models.author.AuthorWithoutBooks;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookCreate {

    private String title;
    private Set<AuthorWithoutBooks> authrors;
}
