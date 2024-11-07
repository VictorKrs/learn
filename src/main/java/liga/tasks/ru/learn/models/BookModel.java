package liga.tasks.ru.learn.models;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookModel {

    private Long id;
    private String title;
    private List<BookAuthorModel> authors;
}
