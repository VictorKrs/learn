package liga.tasks.ru.learn.dto.book;

import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import liga.tasks.ru.learn.dto.author.AuthorWithoutBooks;
import liga.tasks.ru.learn.interfaces.DefaultBookFields;
import liga.tasks.ru.learn.interfaces.IdField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Информация о произведении")
@AllArgsConstructor
@NoArgsConstructor
public class BookModel implements DefaultBookFields, IdField{

    @NotNull
    @Min(1)
    @Schema(description = "Id произведения", example = "2")
    private Long id;
    @NotBlank
    @Schema(description = "Наименование произведения", example = "Винни-Пух")
    private String title;
    @Schema(description = "Список авторов произведения")
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
