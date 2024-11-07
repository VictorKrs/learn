package liga.tasks.ru.learn.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.models.AuthorModel;
import liga.tasks.ru.learn.models.BookAuthorModel;

public class AuthorFactory {

    public static AuthorModel createAuthorModel(Author author) {
        return AuthorModel.builder()
                    .id(author.getId())
                    .name(author.getName())
                    .books(Optional.ofNullable(author.getBooks())
                        .map(books -> books.stream()
                                        .map(BookFactory::creatBookModel).collect(Collectors.toList()))
                        .orElse(new ArrayList<>()))
                    .build();
    }
    
    public static BookAuthorModel createBookAuthorModel(Author author) {
        return BookAuthorModel.builder()
            .id(author.getId())
            .name(author.getName())
            .build();
    }
 
    public static Author createAuthor(AuthorModel author) {
        return Author.builder()
                .id(author.getId())
                .name(author.getName())
                .books(BookFactory.createBooks(author.getBooks()))
                .build();
    }

    public static Author createAuthor(BookAuthorModel author) {
        return Author.builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }

    public static Set<Author> createAuthors(List<BookAuthorModel> authors) {
        return authors.stream()
                .map(AuthorFactory::createAuthor)
                .collect(Collectors.toSet());
    }
}
