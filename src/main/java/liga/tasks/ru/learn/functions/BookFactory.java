package liga.tasks.ru.learn.functions;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.models.BookModel;

public class BookFactory {

    public static BookModel creatBookModel(Book book) {
        return BookModel.builder()
            .id(book.getId())
            .title(book.getTitle())
            .authors(book.getAuthors().stream()
                .map(AuthorFactory::createBookAuthorModel)
                .collect(Collectors.toList()))
            .build();
    }

    public static Book createBook(BookModel book) {
        return Book.builder()
                .title(book.getTitle())
                .authors(null)
                .build();
    }

    public static Set<Book> createBooks(List<BookModel> books) {
        return Optional.ofNullable(books)
            .map(booksList -> booksList.stream()
                .map(BookFactory::createBook)
                .collect(Collectors.toSet()))
            .orElse(new HashSet<>());
    }
}
