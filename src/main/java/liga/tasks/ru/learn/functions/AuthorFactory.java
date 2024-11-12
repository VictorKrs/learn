package liga.tasks.ru.learn.functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.interfaces.IdField;
import liga.tasks.ru.learn.models.author.AuthorModel;

public class AuthorFactory {

    public static Author getAuthor(DefaultAuthorFields authorIn) {
        return Author.builder()
            .id(authorIn instanceof IdField ? ((IdField) authorIn).getId() : null)
            .name(authorIn.getName())
            .books(Optional.ofNullable(authorIn.getBooks())
                .map(books -> books.stream().map(BookFactory::getBook).collect(Collectors.toSet()))
                .orElse(new HashSet<>()))
            .build();
    }


    public static AuthorModel getAuthorModel(Author author) {
        return AuthorModel.builder()
            .id(author.getId())
            .name(author.getName())
            .books(Optional.ofNullable(author.getBooks())
                .map(books -> books.stream().map(BookFactory::getBookWithoutAuthors).collect(Collectors.toList()))
                .orElse(new ArrayList<>()))
            .build();
    }
}