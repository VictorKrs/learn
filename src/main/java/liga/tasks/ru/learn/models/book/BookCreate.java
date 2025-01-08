package liga.tasks.ru.learn.models.book;

import java.util.Objects;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import liga.tasks.ru.learn.interfaces.DefaultBookFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Набор данных для добавления произведения")
@AllArgsConstructor
@NoArgsConstructor
public class BookCreate implements DefaultBookFields {

    @NotBlank
    @Schema(description = "Уникальное название произведения", example = "Винни-Пух")
    private String title;
    @Schema(description = "Список id авторов произведения")
    private Set<Long> authors;

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
