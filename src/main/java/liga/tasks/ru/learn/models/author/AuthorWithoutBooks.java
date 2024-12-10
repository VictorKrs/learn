package liga.tasks.ru.learn.models.author;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import liga.tasks.ru.learn.interfaces.IdField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Информация об авторе произведения")
public class AuthorWithoutBooks  implements IdField {

    @NotNull
    @Min(1)
    @Schema(description = "Id автора", example = "1")
    private Long id;
    @Schema(description = "Имя автора", example = "Алан Милн")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || !o.getClass().getName().equals(this.getClass().getName())){
            return false;
        }

        AuthorWithoutBooks author = (AuthorWithoutBooks) o;

        return this.id.equals(author.id) && this.name.equals(author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
