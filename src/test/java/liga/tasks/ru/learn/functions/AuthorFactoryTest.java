package liga.tasks.ru.learn.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.models.author.AuthorWithoutBooks;
import liga.tasks.ru.learn.models.book.BookWithoutAuthors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuthorFactoryTest {

    private static final String AUTHOR_SECOND_NAME = "Ivanov";
    private static final String AUTHOR_FIRST_NAME = "Ivan";
    private static final String AUTHOR_MIDDLE_NAME = "Ivanovich";
    private static final Long AUTHOR_ID = 1L;
    private static final Set<Book> BOOKS = new HashSet<Book>() {{ add(getBook(1L)); add(getBook(2L)); add(getBook(3L)); }};
    private static final List<BookWithoutAuthors> BOOKS_WITHOUT_AUTHORS = Arrays.asList(getBookWithoutAuthors(1L), getBookWithoutAuthors(2L), getBookWithoutAuthors(3L));


    private static Stream<Arguments> getAuthorTestParameters_AuthorCreate() {
        return Stream.of(
            Arguments.of(AuthorCreate.builder().secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).books(new HashSet<Long>(Collections.singletonList(1L))).build()),
            Arguments.of(AuthorCreate.builder().secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build())
        );
    }

    private static Stream<Arguments> getAuthorTestParameters_AuthorModel() {
        return Stream.of(
            Arguments.of(AuthorModel.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).books(BOOKS_WITHOUT_AUTHORS.stream().collect(Collectors.toList())).build()),
            Arguments.of(AuthorModel.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build())
        );
    }

    private static Stream<Arguments> getAuthorModelTestParamerers() {
        return Stream.of(
            Arguments.of(Author.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).books(BOOKS).build()),
            Arguments.of(Author.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build())
        );
    }

    @ParameterizedTest
    @MethodSource("getAuthorTestParameters_AuthorCreate")
    public void getAuthorTest_fromAuthorCreate_good(AuthorCreate authorIn) {
        log.info("Test data: {}", authorIn);
        testTemplate(mockFactory -> {
            mockFactory.when(() -> BookFactory.getBook(anyLong())).thenAnswer(args -> convertIdToBook(args.getArgument(0)));

            Author author = AuthorFactory.getAuthor(authorIn);

            checkDefaultAuthorFields(author);
            
            Set<Book> expectedBooks = Optional.ofNullable(authorIn.getBooks())
                                            .map(books -> books.stream().map(this::convertIdToBook).collect(Collectors.toSet()))
                                            .orElse(null);

            assertEquals(expectedBooks, author.getBooks());
        });
    }

    @ParameterizedTest
    @MethodSource("getAuthorTestParameters_AuthorModel")
    public void getAuthorTest_fromAuthorModel_good(AuthorModel authorIn) {
        log.info("Test data: {}", authorIn);
        testTemplate(mockFactory -> {
            mockFactory.when(() -> BookFactory.getBook(any(BookWithoutAuthors.class))).thenAnswer(args -> convertToBook(args.getArgument(0)));

            Author author = AuthorFactory.getAuthor(authorIn);

            checkDefaultAuthorFields(author);

            Set<Book> expectedBooks = Optional.ofNullable(authorIn.getBooks())
                                            .map(books -> books.stream().map(this::convertToBook).collect(Collectors.toSet()))
                                            .orElse(null);

            assertEquals(expectedBooks, author.getBooks());
        });
    }

    @ParameterizedTest
    @MethodSource("getAuthorModelTestParamerers")
    public void getAuthorModelTest_good(Author author) {
        log.info("Test data: {}", author);

        testTemplate(mockFactory -> {
            mockFactory.when(() -> BookFactory.getBookWithoutAuthors(any(Book.class))).thenAnswer(args -> convertToBookWithoutAuthors(args.getArgument(0)));

            AuthorModel authorModel = AuthorFactory.getAuthorModel(author);

            checkDefaultAuthorFields(authorModel);
            Set<BookWithoutAuthors> expectedBooks = Optional.ofNullable(author.getBooks())
                                        .map(books -> books.stream().map(this::convertToBookWithoutAuthors).collect(Collectors.toSet()))
                                        .orElse(null);

            assertEquals(expectedBooks, authorModel.getBooks());
        });
    }

    @Test
    public void getAuthorTest_authorWithoutBooks_good() {
        AuthorWithoutBooks author = AuthorWithoutBooks.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build();

        Author result = AuthorFactory.getAuthor(author);
        assertEquals(AUTHOR_ID, result.getId());
        checkDefaultAuthorFields(result);
        assertNull(result.getBooks());
    }

    @Test
    public void getAuthorWithoutBooksTest() {
        Author author = Author.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build();
        AuthorWithoutBooks result = AuthorFactory.geAuthorWithoutBooks(author);

        assertEquals(AUTHOR_ID, result.getId());
        checkDefaultAuthorFields(result);
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

    private void checkDefaultAuthorFields(DefaultAuthorFields author) {
        assertEquals(AUTHOR_SECOND_NAME, author.getSecondName());
        assertEquals(AUTHOR_FIRST_NAME, author.getFirstName());
        assertEquals(AUTHOR_MIDDLE_NAME, author.getMiddleName());
    }

    private Book convertIdToBook(Long id) {
        return Book.builder().id(id).build();
    }

    private Book convertToBook(BookWithoutAuthors book) {
        return Book.builder().id(book.getId()).title(book.getTitle()).build();
    }

    private BookWithoutAuthors convertToBookWithoutAuthors(Book book) {
        return BookWithoutAuthors.builder().id(book.getId()).title(book.getTitle()).build();
    }
}
