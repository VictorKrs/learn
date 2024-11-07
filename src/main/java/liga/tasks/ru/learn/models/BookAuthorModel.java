package liga.tasks.ru.learn.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BookAuthorModel {
    private Long id;
    private String name;
}
