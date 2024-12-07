package liga.tasks.ru.learn.models.book;

import java.util.List;
import java.util.Objects;

import liga.tasks.ru.learn.interfaces.DefaultBookFields;
import liga.tasks.ru.learn.interfaces.IdField;
import liga.tasks.ru.learn.models.author.AuthorWithoutBooks;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookModel implements DefaultBookFields, IdField{

    private Long id;
    private String title;
    private List<AuthorWithoutBooks> authors;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !o.getClass().getName().equals(this.getClass().getName())) {
            return false;
        }

        BookModel book = (BookModel) o;

        return this.id.equals(book.id) && this.title.equals(book.title) && Objects.equals(this.authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authors);
    }
}
