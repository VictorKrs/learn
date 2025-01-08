package liga.tasks.ru.learn.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import lombok.extern.log4j.Log4j2;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    }, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthorRepository.class))
@Log4j2
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositoryTest {
    private final String AUTHOR_SECOND_NAME = "Ivanov";
    private final String AUTHOR_FIRST_NAME = "Ivan";
    private final String AUTHOR_MIDDLE_NAME = "Ivanovich";
    private final String AUTHOR_NAME_FIND = "author name find";
    private final String CHANGED_AUTHOR_NAME = "AuThor Name Changed";
    private final String BOOK_TITLE_1 = "Book1";
    private final String BOOK_TITLE_2 = "Book2";

    private Set<Book> BOOKS;
    
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void clear() {
        BOOKS = Arrays.asList(BOOK_TITLE_1, BOOK_TITLE_2).stream()
            .map(title -> Book.builder().title(title).build())
            .map(bookRepository::save)
            .collect(Collectors.toSet());
    }


    @Test
    public void insertAuthorTest_withoutBooks_good() {
        var result = authorRepository.save(getAuthorDefault());

        checkAuthor(result);
        assertEquals(null, result.getBooks());
    }

    @Test
    public void insertAuthorTest_withBooks_good() {
        var author = authorRepository.save(getAuthorWithBooks());

        var result = authorRepository.findById(author.getId()).orElse(null);

        checkAuthor(result);
        checkBooks(result);
        
    }

    @Test
    public void findByNameTest_notExist_OptionalEmpty() {
        assertFalse(authorRepository.findBySecondNameAndFirstNameAndMiddleName(AUTHOR_SECOND_NAME, AUTHOR_NAME_FIND, AUTHOR_MIDDLE_NAME).isPresent());
    }

    @Test
    public void findBySecondNameAndFirstNameAndMiddleNameTest_exist_OptionalWithAuthor() {
        authorRepository.save(getAuthor(AUTHOR_NAME_FIND));

        Author result = authorRepository.findBySecondNameAndFirstNameAndMiddleName(AUTHOR_SECOND_NAME, AUTHOR_NAME_FIND, AUTHOR_MIDDLE_NAME).orElse(null);
        // Author result = authorRepository.findBySecondName(AUTHOR_SECOND_NAME).orElse(null);

        assertNotNull(result);
        assertTrue(result.getId() > 0);
        
        checkFullName(result, AUTHOR_NAME_FIND);
    }

    @Test
    public void updateTest_good() {
        Author author = authorRepository.save(getAuthorDefault());

        author.setFirstName(CHANGED_AUTHOR_NAME);
        author.setBooks(BOOKS);

        authorRepository.save(author);

        assertTrue(authorRepository.findBySecondNameAndFirstNameAndMiddleName(AUTHOR_SECOND_NAME, AUTHOR_FIRST_NAME, AUTHOR_MIDDLE_NAME).isEmpty());
        var result = authorRepository.findBySecondNameAndFirstNameAndMiddleName(AUTHOR_SECOND_NAME, CHANGED_AUTHOR_NAME, AUTHOR_MIDDLE_NAME).orElseGet(null);
        assertNotNull(result);
        assertEquals(author.getId(), result.getId());
        checkBooks(result);
    }

    @Test
    public void deleteByIdTest_good() {
        var author = authorRepository.save(getAuthorWithBooks());
        authorRepository.deleteById(author.getId());

        assertTrue(authorRepository.findById(author.getId()).isEmpty());
        BOOKS.forEach(book -> {
            assertFalse(bookRepository.findById(book.getId()).isPresent());
        });
    }

    private Author getAuthorDefault() {
        return getAuthor(AUTHOR_FIRST_NAME);
    }
    private Author getAuthor(String firstName) {
        return Author.builder().secondName(AUTHOR_SECOND_NAME).firstName(firstName).middleName(AUTHOR_MIDDLE_NAME).build();
    }

    private Author getAuthorWithBooks() {
        var author = getAuthorDefault();
        
        author.setBooks(BOOKS);

        return author;
    }

    private void checkAuthor(Author result) {
        
        log.info("Author: {}", result);

        assertNotNull(result);
        checkFullName(result, AUTHOR_FIRST_NAME);
        assertTrue(result.getId() > 0);
    }

    private void checkBooks(Author author) {
        assertNotNull(author.getBooks());
        assertNotEquals(0, author.getBooks().size());
        author.getBooks().forEach(book -> {
            assertNotNull(book);
            assertEquals(book.getTitle(), bookRepository.findById(book.getId()).get().getTitle());
        });
    }

    private void checkFullName(Author author, String expectedFirstName) {
        assertEquals(AUTHOR_SECOND_NAME, author.getSecondName());
        assertEquals(expectedFirstName, author.getFirstName());
        assertEquals(AUTHOR_MIDDLE_NAME, author.getMiddleName());
    }
}
