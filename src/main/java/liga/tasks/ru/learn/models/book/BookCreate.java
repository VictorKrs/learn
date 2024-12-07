package liga.tasks.ru.learn.models.book;

import java.util.List;
import java.util.Objects;

import liga.tasks.ru.learn.interfaces.DefaultBookFields;
import liga.tasks.ru.learn.models.author.AuthorWithoutBooks;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookCreate implements DefaultBookFields {

    private String title;
    private List<AuthorWithoutBooks> authors;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !o.getClass().getName().equals(this.getClass().getName())) {
            return false;
        }

        BookCreate book = (BookCreate) o;

        return this.title.equals(book.title) && Objects.equals(authors, book.getAuthors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authors);
    }
}
