package liga.tasks.ru.learn.functions;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;

public class AuthorFactory {

    public static Author getAuthor(AuthorCreate authorIn) {
        return Author.builder()
            .name(authorIn.getName())
            .books(authorIn.getBooks().stream().map(BookFactory::getBook).collect(Collectors.toSet()))
            .build();
    }

    public static AuthorModel getAuthorModel(Author author) {
        return AuthorModel.builder()
            .id(author.getId())
            .name(author.getName())
            .books(Optional.ofNullable(author.getBooks())
                .map(books -> books.stream()
                        .map(BookFactory::getBookWithoutAuthors).collect(Collectors.toList()))
                .orElse(new ArrayList<>()))
            .build();
    }
     
    public static Author getAuthor(AuthorModel author) {
        return Author.builder()
            .id(author.getId())
            .name(author.getName())
            .books(Optional.of(author.getBooks())
                    .map(books -> books.stream()
                            .map(BookFactory::getBook)
                            .collect(Collectors.toSet()))
                    .orElse(null))
            .build();
    }
}
