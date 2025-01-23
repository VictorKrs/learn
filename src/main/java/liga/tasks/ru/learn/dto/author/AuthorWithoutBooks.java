package liga.tasks.ru.learn.dto.author;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.interfaces.IdField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Информация об авторе произведения")
@AllArgsConstructor
@NoArgsConstructor
public class AuthorWithoutBooks  implements IdField, DefaultAuthorFields {

    @NotNull
    @Min(1)
    @Schema(description = "Id автора", example = "1")
    private Long id;
    @Schema(description = "Фамилия автора", example = "Булгаков")
    private String secondName;
    @Schema(description = "Имя автора", example = "Михаил")
    private String firstName;
    @Schema(description = "Отчество автора", example = "Афанасьевич")
    private String middleName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || !o.getClass().getName().equals(this.getClass().getName())){
            return false;
        }

        AuthorWithoutBooks author = (AuthorWithoutBooks) o;

        return this.id.equals(author.id) && this.getFullName().equals(author.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getFullName());
    }
}
