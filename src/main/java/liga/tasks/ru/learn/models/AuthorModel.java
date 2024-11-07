package liga.tasks.ru.learn.models;

import java.util.List;
import java.util.stream.Collectors;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.functions.BookFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorModel {
    private Long id;
    private String name;

    private List<BookModel> books;

    public static AuthorModel createAuthorModel(Author author) {
        return AuthorModel.builder()
            .id(author.getId())
            .name(author.getName())
            .books(author.getBooks().stream()
                .map(BookFactory::creatBookModel)
                .collect(Collectors.toList()))
            .build();
    }
} 
