package liga.tasks.ru.learn.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.interfaces.DefaultBookFields;
import liga.tasks.ru.learn.interfaces.IdField;
import liga.tasks.ru.learn.models.author.AuthorWithoutBooks;
import liga.tasks.ru.learn.models.book.BookCreate;
import liga.tasks.ru.learn.models.book.BookModel;
import liga.tasks.ru.learn.models.book.BookWithoutAuthors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BookFactoryTest {
    public final static String TITLE = "title";
    public final static Long BOOK_ID = 1L;
    private final static String AUTHOR = "author";

    @Test
    public void getBookTest_bookWithoutAuthors_good() {
        BookWithoutAuthors book = BookWithoutAuthors.builder().id(BOOK_ID).title(TITLE).build();

        Book result = BookFactory.getBook(book);

        assertEquals(BOOK_ID, result.getId());
        assertEquals(TITLE, result.getTitle());
        assertNull(result.getAuthors());
    }

    @Test
    public void getBookTest_notIdField_notEmptyAuthors_good() {
        BookCreate book = BookCreate.builder().title(TITLE).authors(getAuthorsWithoutbooks()).build();

        testTemplate(mockFactory -> {
            mockFactory.when(() -> AuthorFactory.getAuthor(any(AuthorWithoutBooks.class))).then(args -> {
                AuthorWithoutBooks author = args.getArgument(0);

                return Author.builder().id(author.getId()).firstName(author.getFirstName()).build();
            });
            
            Book result = BookFactory.getBook(book);
            
            checkResult(book, result);
        });
    }

    @Test
    public void getBookTest_idField_emptyAuthors_good() {
        BookModel book = BookModel.builder().id(BOOK_ID).title(TITLE).build();

        testTemplate(ignored -> {
            Book result = BookFactory.getBook(book);
            
            checkResult(book, result);
        });
    }

    @Test
    public void getBookWithoutAuthorsTest_good() {
        Book book = Book.builder().id(BOOK_ID).title(TITLE).build();

        BookWithoutAuthors result = BookFactory.getBookWithoutAuthors(book);

        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    public void getBookModelTest_good() {
        Book book = Book.builder().id(BOOK_ID).title(TITLE).authors(getAuthors()).build();

        testTemplate(mockFactory -> {
            mockFactory.when(() -> AuthorFactory.geAuthorWithoutBooks(any(Author.class))).thenAnswer(args -> {
                Author author = args.getArgument(0);
                return AuthorWithoutBooks.builder().id(author.getId()).firstName(author.getFirstName()).build();
            });

            BookModel result = BookFactory.getBookModel(book);

            checkResult(result, book);
        });
    }

    private void testTemplate(Consumer<MockedStatic<AuthorFactory>> action) {
        try (MockedStatic<AuthorFactory> mockAuthorFactory = mockStatic(AuthorFactory.class)) {
            action.accept(mockAuthorFactory);
        }
    }

    private List<AuthorWithoutBooks> getAuthorsWithoutbooks() {
        Long id = 10L;
        return Arrays.asList(
            AuthorWithoutBooks.builder().id(id).firstName(AUTHOR + "_" + id++).build(),
            AuthorWithoutBooks.builder().id(id).firstName(AUTHOR + "_" + id++).build(),
            AuthorWithoutBooks.builder().id(id).firstName(AUTHOR + "_" + id++).build(),
            AuthorWithoutBooks.builder().id(id).firstName(AUTHOR + "_" + id++).build()
        );
    }

    private Set<Author> getAuthors() {
        return new HashSet<Author>() {{
            Long id = 10L;
            add(Author.builder().id(id).firstName(AUTHOR + "_" + id++).build());
            add(Author.builder().id(id).firstName(AUTHOR + "_" + id++).build());
            add(Author.builder().id(id).firstName(AUTHOR + "_" + id++).build());
            add(Author.builder().id(id).firstName(AUTHOR + "_" + id++).build());
        }};
    }

    private void checkResult(DefaultBookFields source, Book result) {
        assertNotNull(result);
        if (source instanceof IdField) {
            assertEquals(((IdField)source).getId(), result.getId());
        } else {
            assertNull(result.getId());
        }
        assertEquals(source.getTitle(), result.getTitle());
        if (source.getAuthors() != null) {
            assertEquals(source.getAuthors().size(), result.getAuthors().size());
            source.getAuthors().forEach(author -> 
                assertTrue(result.getAuthors().stream()
                    .anyMatch(resAuthor -> author.getId().equals(resAuthor.getId()) 
                            && author.getFullName().equals(resAuthor.getFullName()))));
        } else {
            assertTrue(result.getAuthors().isEmpty());
        }
    }
}
