package liga.tasks.ru.learn.models.author;

import java.util.List;

import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.interfaces.IdField;
import liga.tasks.ru.learn.models.book.BookWithoutAuthors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorModel implements DefaultAuthorFields, IdField {
    private Long id;
    private String name;

    private List<BookWithoutAuthors> books;
} 
