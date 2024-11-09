package liga.tasks.ru.learn.models.author;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorWithoutBooks {

    private Long id;
    private String name;
}
