package liga.tasks.ru.learn.functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import liga.tasks.ru.learn.dto.author.AuthorCreate;
import liga.tasks.ru.learn.dto.author.AuthorModel;
import liga.tasks.ru.learn.dto.author.AuthorWithoutBooks;
import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.interfaces.IdField;

public class AuthorFactory {

    public static Author getAuthor(AuthorModel authorIn) {
        return getAuthorBuilderDefault(authorIn)
            .books(Optional.ofNullable(authorIn.getBooks())
                .map(books -> books.stream().map(BookFactory::getBook).collect(Collectors.toSet()))
                .orElse(new HashSet<>()))
            .build();
    }

    public static Author getAuthor(AuthorCreate authorIn) {
        return getAuthorBuilderDefault(authorIn)
            .books(Optional.ofNullable(authorIn.getBooks())
                .map(books -> books.stream().map(BookFactory::getBook).collect(Collectors.toSet()))
                .orElse(new HashSet<>()))
            .build();
    }

    public static Author getAuthor(Long id) {
        return Author.builder().id(id).build();
    }

    public static Author getAuthor(AuthorWithoutBooks author){
        return Author.builder()
            .id(author.getId())
            .secondName(author.getSecondName())
            .firstName(author.getFirstName())
            .middleName(author.getMiddleName())
            .build();
    }

    public static AuthorModel getAuthorModel(Author author) {
        return AuthorModel.builder()
            .id(author.getId())
            .secondName(author.getSecondName())
            .firstName(author.getFirstName())
            .middleName(author.getMiddleName())
            .books(Optional.ofNullable(author.getBooks())
                .map(books -> books.stream().map(BookFactory::getBookWithoutAuthors).collect(Collectors.toList()))
                .orElse(new ArrayList<>()))
            .build();
    }

    public static AuthorWithoutBooks geAuthorWithoutBooks(Author author) {
        return AuthorWithoutBooks.builder()
            .id(author.getId())
            .secondName(author.getSecondName())
            .firstName(author.getFirstName())
            .middleName(author.getMiddleName())
            .build();
    }

    private static Author.AuthorBuilder getAuthorBuilderDefault(DefaultAuthorFields authorIn) {
        return Author.builder()
            .id(authorIn instanceof IdField ? ((IdField) authorIn).getId() : null)
            .secondName(authorIn.getSecondName())
            .firstName(authorIn.getFirstName())
            .middleName(authorIn.getMiddleName());
    }
}