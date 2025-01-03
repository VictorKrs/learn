package liga.tasks.ru.learn.models.author;

import java.util.List;
import java.util.Objects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.models.book.BookWithoutAuthors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Новый автор")
public class AuthorCreate implements DefaultAuthorFields {

    @NotBlank
    @Schema(description = "Имя автора", example = "Алан Милан")
    private String name;
    @Schema(description = "Список произведений автора")
    private List<BookWithoutAuthors> books;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !o.getClass().getName().equals(this.getClass().getName())) {
            return false;
        }

        AuthorCreate author = (AuthorCreate) o;

        return this.name.equals(author.name) && Objects.equals(this.books, author.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, books);
    }
}
