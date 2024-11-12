package liga.tasks.ru.learn.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.interfaces.IdField;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.models.book.BookWithoutAuthors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuthorFactoryTest {

    private static final String AUTHOR_NAME = "Ivanov Ivan";
    private static final Long AUTHOR_ID = 1L;
    private static final Set<Book> BOOKS = new HashSet<Book>() {{ add(getBook(1L)); add(getBook(2L)); add(getBook(3L)); }};
    private static final List<BookWithoutAuthors> BOOKS_WITHOUT_AUTHORS = Arrays.asList(getBookWithoutAuthors(1L), getBookWithoutAuthors(2L), getBookWithoutAuthors(3L));


    private static Stream<Arguments> getAuthorTestParamerers() {
        return Stream.of(
            Arguments.of(AuthorCreate.builder().name(AUTHOR_NAME).books(BOOKS_WITHOUT_AUTHORS).build()),
            Arguments.of(AuthorCreate.builder().name(AUTHOR_NAME).build()),
            Arguments.of(AuthorModel.builder().id(AUTHOR_ID).name(AUTHOR_NAME).books(BOOKS_WITHOUT_AUTHORS.stream().collect(Collectors.toList())).build()),
            Arguments.of(AuthorModel.builder().id(AUTHOR_ID).name(AUTHOR_NAME).build())
        );
    }

    private static Stream<Arguments> getAuthorModelTestParamerers() {
        return Stream.of(
            Arguments.of(Author.builder().id(AUTHOR_ID).name(AUTHOR_NAME).books(BOOKS).build()),
            Arguments.of(Author.builder().id(AUTHOR_ID).name(AUTHOR_NAME).build())
        );
    }

    @ParameterizedTest
    @MethodSource("getAuthorTestParamerers")
    public void getAuthorTest_good(DefaultAuthorFields authorIn) {
        log.info("Test data: {}", authorIn);
        testTemplate(mockFactory -> {
            mockGetBook(mockFactory);

            Author author = AuthorFactory.getAuthor(authorIn);

            check(authorIn, author);
        });
    }

    @ParameterizedTest
    @MethodSource("getAuthorModelTestParamerers")
    public void getAuthorModelTest_good(Author author) {
        log.info("Test data: {}", author);

        testTemplate(mockFactory -> {
            mockGetBookWithoutAuthors(mockFactory);

            AuthorModel authorModel = AuthorFactory.getAuthorModel(author);

            check(authorModel, author);
        });
    }

    private static Book getBook(Long id) {
        return Book.builder().id(id).title("book_" + id).build();
    }

    private static BookWithoutAuthors getBookWithoutAuthors(Long id) {
        return BookWithoutAuthors.builder().id(id).title("book_" + id).build();
    }


    private void testTemplate(Consumer<MockedStatic<BookFactory>> action) {
        try (MockedStatic<BookFactory> bookFactoryMocked = mockStatic(BookFactory.class)) {

            action.accept(bookFactoryMocked);
        }
    }

    private void mockGetBook(MockedStatic<BookFactory> mockFactory) {
        mockFactory.when(() -> BookFactory.getBook(any(BookWithoutAuthors.class))).thenAnswer(args -> {
            BookWithoutAuthors book = args.getArgument(0);
            
            return Book.builder().id(book.getId()).title(book.getTitle()).build();
        });
    }

    private void mockGetBookWithoutAuthors(MockedStatic<BookFactory> mockFactory) {
        mockFactory.when(() -> BookFactory.getBookWithoutAuthors(any(Book.class))).thenAnswer(args -> {
            Book book = args.getArgument(0);

            return BookWithoutAuthors.builder().id(book.getId()).title(book.getTitle()).build();
        });
    }

    private void check(DefaultAuthorFields authorModel, Author author) {
        if (authorModel instanceof IdField) {
            assertEquals(((IdField)authorModel).getId(), author.getId());
        } else {
            assertNull(author.getId());
        }
        assertEquals(authorModel.getName(), author.getName());
        assertEquals(authorModel.getBooks() == null || authorModel.getBooks().size() == 0, author.getBooks() == null || author.getBooks().size() == 0);

        if (authorModel.getBooks() != null && author.getBooks() != null) {
            assertEquals(authorModel.getBooks().size(), author.getBooks().size());
            authorModel.getBooks().forEach(bookIn -> assertTrue(author.getBooks().stream().anyMatch(bookOut -> {
                return Objects.equals(bookIn.getId(), bookOut.getId()) && Objects.equals(bookIn.getTitle(), bookOut.getTitle());
            })));
        }
    }
}
