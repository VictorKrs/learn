package liga.tasks.ru.learn.models.book;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookWithoutAuthors {

    private Long id;
    private String title;
}
