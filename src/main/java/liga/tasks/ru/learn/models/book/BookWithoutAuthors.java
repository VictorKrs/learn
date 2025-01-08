package liga.tasks.ru.learn.models.book;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Информация о произведении без авторов")
@AllArgsConstructor
@NoArgsConstructor
public class BookWithoutAuthors implements IdField, DefaultBookFields {

    @NotNull
    @Min(1)
    @Schema(description = "Id произведения", example = "1")
    private Long id;
    @Schema(description = "Наименование произведения", example = "Винни-Пух")
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
