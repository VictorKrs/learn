package liga.tasks.ru.learn.models.author;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null || !this.getClass().getName().equals(other.getClass().getName())) {
            return false;
        }

        AuthorModel author = (AuthorModel) other;

        return Objects.equals(this.id, author.id) && this.name.equals(author.name) && Objects.equals(this.books, author.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, books);
    }
} 
