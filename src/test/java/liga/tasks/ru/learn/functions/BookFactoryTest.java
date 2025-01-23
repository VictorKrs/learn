package liga.tasks.ru.learn.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import liga.tasks.ru.learn.dto.author.AuthorWithoutBooks;
import liga.tasks.ru.learn.dto.book.BookCreate;
import liga.tasks.ru.learn.dto.book.BookModel;
import liga.tasks.ru.learn.dto.book.BookWithoutAuthors;
import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.interfaces.DefaultBookFields;
import liga.tasks.ru.learn.interfaces.IdField;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BookFactoryTest {
    public final static String TITLE = "title";
    public final static Long BOOK_ID = 1L;
    public final static Long AUTHOR_ID = 10L;
    private final static String AUTHOR = "author";

    @Test
    public void getBookTest_fromId_good() {
        Book result = BookFactory.getBook(BOOK_ID);

        assertEquals(BOOK_ID, result.getId());
    }

    @Test
    public void getBookTest_fromBookWithoutAuthors_good() {
        BookWithoutAuthors book = BookWithoutAuthors.builder().id(BOOK_ID).title(TITLE).build();

        Book result = BookFactory.getBook(book);

        checkDefaultBookFieldsAndId(result);
        assertNull(result.getAuthors());
    }

    @Test
    public void getBookTest_fromBookCreate_notEmptyAuthors_good() {
        Set<Long> authorsId = new HashSet<Long>() {{ add(AUTHOR_ID); }};
        BookCreate book = BookCreate.builder().title(TITLE).authors(authorsId).build();

        testTemplate(mockFactory -> {
            mockFactory.when(() -> AuthorFactory.getAuthor(anyLong())).thenAnswer(args -> convertIdToAuthor(args.getArgument(0)));
            
            Book result = BookFactory.getBook(book);
            
            checkDefaultBookFields(result);
            assertNull(result.getId());
            assertEquals(authorsId.stream().map(this::convertIdToAuthor).collect(Collectors.toSet()), result.getAuthors());
        });
    }

    @Test
    public void getBookTest_fromBookCreate_emptyAuthors_good() {
        BookCreate book = BookCreate.builder().title(TITLE).build();

        testTemplate(mockFactory -> {
            Book result = BookFactory.getBook(book);
            
            checkDefaultBookFields(result);
            assertNull(result.getId());
            assertEquals(new HashSet<>(), result.getAuthors());
        });
    }

    @Test
    public void getBookTest_fromBookModel_notEmptyAuthors_good() {
        List<AuthorWithoutBooks> authors = Arrays.asList(AuthorWithoutBooks.builder().firstName(AUTHOR).id(AUTHOR_ID).build());
        BookModel book = BookModel.builder().id(BOOK_ID).title(TITLE).authors(authors).build();

        testTemplate(mockFactory -> {
            mockFactory.when(() -> AuthorFactory.getAuthor(any(AuthorWithoutBooks.class))).thenAnswer(args -> convertAuthorWithoutBooksToAuthor(args.getArgument(0)));
            
            Book result = BookFactory.getBook(book);
            
            checkDefaultBookFieldsAndId(result);
            assertEquals(authors.stream().map(this::convertAuthorWithoutBooksToAuthor).collect(Collectors.toSet()), result.getAuthors());
        });
    }

    @Test
    public void getBookTest_fromBookModel_emptyAuthors_good() {
        BookModel book = BookModel.builder().id(BOOK_ID).title(TITLE).build();

        testTemplate(mockFactory -> {
            Book result = BookFactory.getBook(book);
            
            checkDefaultBookFieldsAndId(result);
            assertEquals(new HashSet<>(), result.getAuthors());
        });
    }

    @Test
    public void getBookWithoutAuthorsTest_good() {
        Book book = Book.builder().id(BOOK_ID).title(TITLE).build();

        BookWithoutAuthors result = BookFactory.getBookWithoutAuthors(book);

        assertNotNull(result);
        checkDefaultBookFieldsAndId(result);
    }

    @Test
    public void getBookModelTest_good() {
        Set<Author> authors = new HashSet<Author>() {{ add(Author.builder().id(AUTHOR_ID).firstName(AUTHOR).build()); }};
        Book book = Book.builder().id(BOOK_ID).title(TITLE).authors(authors).build();

        testTemplate(mockFactory -> {
            mockFactory.when(() -> AuthorFactory.geAuthorWithoutBooks(any(Author.class))).thenAnswer(args -> convertAuthorToAuthorWithoutBooks(args.getArgument(0)));

            BookModel result = BookFactory.getBookModel(book);
            
            checkDefaultBookFieldsAndId(result);
            assertEquals(authors.stream().map(this::convertAuthorToAuthorWithoutBooks).collect(Collectors.toList()), result.getAuthors());
        });
    }

    private void testTemplate(Consumer<MockedStatic<AuthorFactory>> action) {
        try (MockedStatic<AuthorFactory> mockAuthorFactory = mockStatic(AuthorFactory.class)) {
            action.accept(mockAuthorFactory);
        }
    }

    private void checkDefaultBookFields(DefaultBookFields book) {
        assertEquals(TITLE, book.getTitle());
    }

    private void checkDefaultBookFieldsAndId(IdField book) {
        checkDefaultBookFields((DefaultBookFields) book);

        assertEquals(BOOK_ID , ((IdField)book).getId());
    }

    private Author convertIdToAuthor(Long id) {
        return Author.builder().id(id).build();
    }

    private Author convertAuthorWithoutBooksToAuthor(AuthorWithoutBooks author) {
        return Author.builder().id(author.getId()).firstName(author.getFirstName()).secondName(author.getSecondName()).middleName(author.getMiddleName()).build();
    }

    private AuthorWithoutBooks convertAuthorToAuthorWithoutBooks(Author author) {
        return AuthorWithoutBooks.builder().id(author.getId()).firstName(author.getFirstName()).secondName(author.getSecondName()).middleName(author.getMiddleName()).build();
    }
}
