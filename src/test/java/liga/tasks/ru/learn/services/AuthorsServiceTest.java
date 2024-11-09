package liga.tasks.ru.learn.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.functions.AuthorFactory;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.repositories.AuthorRepository;
import liga.tasks.ru.learn.services.exceptions.AuthorAlreadyExistException;
import liga.tasks.ru.learn.services.exceptions.AuthorNotFoundException;

@SpringBootTest(classes = AuthorsService.class)
public class AuthorsServiceTest {
    private String AUTHOR_NAME = "Ivanov Ivan";
    private String CHANGED_AUTHOR_NAME = "Ivanov Ivan CHANGED";
    private Long AUTHOR_ID = 1L;

    @MockBean
    private AuthorRepository authorRepository;
    
    @Autowired
    private AuthorsService authorsService;

    @Test
    public void saveTest_authorAlreadyExist_AuthorAlreadyExistException() {
        Author author = getAuthor();
        when(authorRepository.findByName(eq(AUTHOR_NAME))).thenReturn(Optional.of(author));
    
        testTemplate(mockFactory -> {
            mockGetAuthorForCreate(mockFactory);

            assertThrows(AuthorAlreadyExistException.class, () -> authorsService.save(getAuthorCreate()));
        });
    }

    @Test
    public void saveTest_good() {
        mockFindByNameWithEmpty(AUTHOR_NAME);
        mockSave();
    
        testTemplate(mockFactory -> {
            mockGetAuthorForCreate(mockFactory);
            mockGetAuthorModel(mockFactory);

            var result = authorsService.save(getAuthorCreate());
            
            assertEquals(AUTHOR_NAME, result.getName());
            assertEquals(AUTHOR_ID, result.getId());
        });
    }

    @Test
    public void findByIdTest_notExist_AuthorNotFoundException() {
        when(authorRepository.findById(eq(AUTHOR_ID))).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorsService.findById(AUTHOR_ID));
    }

    @Test
    public void findByIdTest_exist_good() {
        mockFindByIdWithResult();

        testTemplate(mockFactory -> {
            mockGetAuthorModel(mockFactory);

            var result = authorsService.findById(AUTHOR_ID);
            assertNotNull(result);
            assertEquals(AUTHOR_ID, result.getId());
            assertEquals(AUTHOR_NAME, result.getName());
        });
    }

    @Test
    public void updateTest_good() {
        mockFindByIdWithResult();
        mockFindByNameWithEmpty(CHANGED_AUTHOR_NAME);
        mockSave();

        testTemplate(mockFactory -> {
            mockFactory.when(() -> AuthorFactory.getAuthor(any(AuthorModel.class))).thenAnswer(args -> {
                AuthorModel model = args.getArgument(0);

                return Author.builder().id(model.getId()).name(model.getName()).build();
            });
            mockGetAuthorModel(mockFactory);


            AuthorModel author = AuthorModel.builder().id(AUTHOR_ID).name(CHANGED_AUTHOR_NAME).build();
            var result = authorsService.update(author);

            assertEquals(author.getId(), result.getId());
            assertEquals(author.getName(), result.getName());
        });
    }

    @Test
    public void deleteByIdTest_good() {
        authorsService.deleteById(AUTHOR_ID);

        verify(authorRepository).deleteById(AUTHOR_ID);
    }   

    private AuthorCreate getAuthorCreate() {
        return AuthorCreate.builder().name(AUTHOR_NAME).build();
    }

    private Author getAuthor() {
        return Author.builder().id(AUTHOR_ID).name(AUTHOR_NAME).build();
    }


    private void mockFindByIdWithResult() {
        Author author = getAuthor();
        when(authorRepository.findById(eq(AUTHOR_ID))).thenReturn(Optional.of(author));
    }

    private void mockFindByNameWithEmpty(String authorName) {
        when(authorRepository.findByName(eq(authorName))).thenReturn(Optional.empty());
    }
    
    private void mockSave() {
        when(authorRepository.save(any(Author.class)))
            .thenAnswer(args -> {
                Author author = args.getArgument(0);
                if (author.getId() == null) {
                    author.setId(AUTHOR_ID);
                }
                return author;
            });
    }

    private void mockGetAuthorForCreate(MockedStatic<AuthorFactory> factoryMock) {
        factoryMock.when(() -> AuthorFactory.getAuthor(any(AuthorCreate.class)))
            .thenReturn(Author.builder().name(AUTHOR_NAME).build());
    }

    private void mockGetAuthorModel(MockedStatic<AuthorFactory> factoryMock) {
        factoryMock.when(() -> AuthorFactory.getAuthorModel(any(Author.class)))
            .thenAnswer(args -> {
                Author author = args.getArgument(0);

                return AuthorModel.builder().id(author.getId()).name(author.getName()).build();
            });
    }

    private void testTemplate(Consumer<MockedStatic<AuthorFactory>> action) {
        try (MockedStatic<AuthorFactory> authorFactoryMock = mockStatic(AuthorFactory.class)) {
            action.accept(authorFactoryMock);
        }
    }
}
