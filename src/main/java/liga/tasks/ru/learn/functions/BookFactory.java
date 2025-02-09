package liga.tasks.ru.learn.functions;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import liga.tasks.ru.learn.dto.book.BookCreate;
import liga.tasks.ru.learn.dto.book.BookModel;
import liga.tasks.ru.learn.dto.book.BookWithoutAuthors;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.interfaces.DefaultBookFields;
import liga.tasks.ru.learn.interfaces.IdField;

public class BookFactory {

    public static Book getBook(Long id) {
        return Book.builder()
                .id(id)
                .build();
    }

    public static Book getBook(BookWithoutAuthors bookIn) {
        return Book.builder()
                .id(bookIn.getId())
                .title(bookIn.getTitle())
                .build();
    }

    public static Book getBook(BookCreate book) {
        return getBookBuilderDefault(book)
                .authors(Optional.ofNullable(book.getAuthors())
                    .map(authors -> authors.stream().map(AuthorFactory::getAuthor).collect(Collectors.toSet()))
                    .orElse(new HashSet<>()))
                .build();
    }

    public static Book getBook(BookModel book) {
        return getBookBuilderDefault(book)
                .authors(Optional.ofNullable(book.getAuthors())
                    .map(authors -> authors.stream().map(AuthorFactory::getAuthor).collect(Collectors.toSet()))
                    .orElse(new HashSet<>()))
                .build();
    }

    public static BookWithoutAuthors getBookWithoutAuthors(Book book) {
        return BookWithoutAuthors.builder().id(book.getId()).title(book.getTitle()).build();
    }

    public static BookModel getBookModel(Book book) {
        return BookModel.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authors(book.getAuthors().stream().map(AuthorFactory::geAuthorWithoutBooks).collect(Collectors.toList()))
                .build();
    }

    private static Book.BookBuilder getBookBuilderDefault(DefaultBookFields book) {
        return Book.builder()
                .id((book instanceof IdField) ? ((IdField)book).getId() : null)
                .title(book.getTitle());
    }
}
