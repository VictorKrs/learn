package liga.tasks.ru.learn.models.book;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookWithoutAuthors {

    private Long id;
    private String title;

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null || !this.getClass().getName().equals(other.getClass().getName())) {
            return false;
        }
        
        BookWithoutAuthors book = (BookWithoutAuthors) other;

        return Objects.equals(this.id, book.id) && Objects.equals(this.title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
