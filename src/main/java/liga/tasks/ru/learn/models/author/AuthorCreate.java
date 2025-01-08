package liga.tasks.ru.learn.models.author;

import java.util.Objects;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Новый автор")
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreate implements DefaultAuthorFields {

    @NotBlank
    @Schema(description = "Фамилия автора", example = "Булгаков")
    private String secondName;
    @NotBlank
    @Schema(description = "Имя автора", example = "Михаил")
    private String firstName;
    @Schema(description = "Отчество автора", example = "Афанасьевич")
    private String middleName;
    @Schema(description = "Список id произведений автора")
    private Set<Long> books;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !o.getClass().getName().equals(this.getClass().getName())) {
            return false;
        }

        AuthorCreate author = (AuthorCreate) o;

        return this.getFullName().equals(author.getFullName()) && Objects.equals(this.books, author.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullName(), books);
    }
}
