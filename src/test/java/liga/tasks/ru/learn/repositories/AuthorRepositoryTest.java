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
    private final String AUTHOR_NAME = "author name";
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
        var result = authorRepository.save(getAuthor());

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
        assertFalse(authorRepository.findByName(AUTHOR_NAME_FIND).isPresent());
    }

    @Test
    public void findByNameTest_exist_OptionalWithAuthor() {
        authorRepository.save(Author.builder().name(AUTHOR_NAME_FIND).build());

        Author result = authorRepository.findByName(AUTHOR_NAME_FIND).orElse(null);

        assertNotNull(result);
        assertTrue(result.getId() > 0);
        assertEquals(AUTHOR_NAME_FIND, result.getName());
    }

    @Test
    public void updateTest_good() {
        Author author = authorRepository.save(getAuthor());

        author.setName(CHANGED_AUTHOR_NAME);
        author.setBooks(BOOKS);

        authorRepository.save(author);

        assertTrue(authorRepository.findByName(AUTHOR_NAME).isEmpty());
        var result = authorRepository.findByName(CHANGED_AUTHOR_NAME).orElseGet(null);
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

    private Author getAuthor() {
        var author = new Author();
        author.setName(AUTHOR_NAME);
        return author;
    }

    private Author getAuthorWithBooks() {
        var author = getAuthor();
        
        author.setBooks(BOOKS);

        return author;
    }

    private void checkAuthor(Author result) {
        
        log.info("Author: {}", result);

        assertNotNull(result);
        assertEquals(AUTHOR_NAME, result.getName());
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
}
