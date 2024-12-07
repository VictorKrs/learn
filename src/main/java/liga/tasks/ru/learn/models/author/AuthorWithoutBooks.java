package liga.tasks.ru.learn.models.author;

import java.util.Objects;

import liga.tasks.ru.learn.interfaces.IdField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorWithoutBooks  implements IdField {

    private Long id;
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
