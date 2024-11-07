package liga.tasks.ru.learn.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import liga.tasks.ru.learn.entities.Author;
import lombok.extern.log4j.Log4j2;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    }, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthorRepository.class))
@Log4j2
public class AuthorRepositoryTest {
    private final String AUTHOR_NAME = "author name";
    
    @Autowired
    private AuthorRepository repository;

    @Test
    public void insertAuthorTest_withoutBooks_good() {
        var author = new Author();
        author.setName(AUTHOR_NAME);

        var result = repository.save(author);

        log.info("Inserted author: {}", result);

        assertNotNull(result);
        assertEquals(AUTHOR_NAME, result.getName());
        assertEquals(null, result.getBooks());
        assertTrue(result.getId() > 0);
    }

}
