package liga.tasks.ru.learn.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.exceptions.AuthorAlreadyExistException;
import liga.tasks.ru.learn.exceptions.AuthorNotFoundException;
import liga.tasks.ru.learn.exceptions.BookNotFoundException;
import liga.tasks.ru.learn.functions.AuthorFactory;
import liga.tasks.ru.learn.interfaces.DefaultAuthorFields;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.repositories.AuthorRepository;
import liga.tasks.ru.learn.repositories.BookRepository;

@SpringBootTest(classes = AuthorsService.class)
public class AuthorsServiceTest {
    private final String AUTHOR_SECOND_NAME = "Ivanov";
    private final String AUTHOR_FIRST_NAME = "Ivan";
    private final String AUTHOR_MIDDLE_NAME = "Ivanovich";
    private final String CHANGED_AUTHOR_NAME = "Ivanov Ivan CHANGED";
    private final Long AUTHOR_ID = 1L;
    private final Set<Long> BOOKS_IDS = new HashSet<Long>() {{ add(1L); }};

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private BookRepository bookRepository;
    
    @Autowired
    private AuthorsService authorsService;

    @Test
    public void saveTest_authorAlreadyExist_AuthorAlreadyExistException() {
        Author author = getAuthor();
        mockFindByFullName(AUTHOR_FIRST_NAME, Optional.of(author));
    
        testTemplate(mockFactory -> {
            mockGetAuthorForCreate(mockFactory);

            assertThrows(AuthorAlreadyExistException.class, () -> authorsService.save(getAuthorCreate(null)));
        });
    }

    @Test
    public void saveTest_booksIdNotFound_BookNotFoundException() {
        mockFindBooks(new ArrayList<>());
        when(bookRepository.findAllById(anyIterable())).thenReturn(new ArrayList<>());

        testTemplate(mockFactory -> {
            mockGetAuthorForCreate(mockFactory);

            assertThrows(BookNotFoundException.class, () -> authorsService.save(getAuthorCreate(BOOKS_IDS)));
        });
    }


    @Test
    public void saveTest_good() {
        mockFindBooks(BOOKS_IDS.stream().map(id -> Book.builder().id(id).build()).collect(Collectors.toList()));
        mockFindByFullName(AUTHOR_FIRST_NAME, Optional.empty());
        mockSave();
        mockFindByIdWithResult();
    
        testTemplate(mockFactory -> {
            mockGetAuthorForCreate(mockFactory);
            mockGetAuthorModel(mockFactory);

            AuthorModel result = authorsService.save(getAuthorCreate(BOOKS_IDS));
            
            checkAuthorDedaultFields(AUTHOR_FIRST_NAME, result);
            assertEquals(AUTHOR_ID, result.getId());
        });
    }

    @Test
    public void findByIdTest_notExist_AuthorNotFoundException() {
        when(authorRepository.findById(eq(AUTHOR_ID))).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorsService.findById(AUTHOR_ID));
    }

    @Test
    public void updateTest_good() {
        mockFindByIdWithResult();
        mockFindByFullName(CHANGED_AUTHOR_NAME, Optional.empty());
        mockSave();

        testTemplate(mockFactory -> {
            mockFactory.when(() -> AuthorFactory.getAuthor(any(AuthorModel.class))).thenAnswer(args -> {
                AuthorModel model = args.getArgument(0);

                return Author.builder().id(model.getId()).secondName(model.getSecondName()).firstName(model.getFirstName()).middleName(model.getMiddleName()).build();
            });
            mockGetAuthorModel(mockFactory);


            AuthorModel author = AuthorModel.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(CHANGED_AUTHOR_NAME).middleName(AUTHOR_MIDDLE_NAME).build();
            var result = authorsService.update(author);

            assertEquals(author.getId(), result.getId());
            checkAuthorDedaultFields(CHANGED_AUTHOR_NAME, result);
        });
    }

    @Test
    public void deleteByIdTest_good() {
        authorsService.deleteById(AUTHOR_ID);

        verify(authorRepository).deleteById(AUTHOR_ID);
    }   

    private AuthorCreate getAuthorCreate(Set<Long> books) {
        return AuthorCreate.builder().secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).books(books).build();
    }

    private Author getAuthor() {
        return Author.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build();
    }


    private void mockFindByIdWithResult() {
        Author author = getAuthor();
        when(authorRepository.findById(eq(AUTHOR_ID))).thenReturn(Optional.of(author));
    }
    
    private void mockFindByFullName(String firstName, Optional<Author> result) {
        when(authorRepository.findBySecondNameAndFirstNameAndMiddleName(eq(AUTHOR_SECOND_NAME), eq(firstName), eq(AUTHOR_MIDDLE_NAME))).thenReturn(result);
    }

    private void mockFindBooks(List<Book> books) {
        when(bookRepository.findAllById(anyIterable())).thenReturn(books);
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
            .thenAnswer(args -> {
                AuthorCreate author = args.getArgument(0);

                return Author.builder()
                    .secondName(author.getSecondName())
                    .firstName(author.getFirstName())
                    .middleName(author.getMiddleName())
                    .books(Optional.ofNullable(author.getBooks())
                                .map(books -> books.stream().map(this::convertIdToBook).collect(Collectors.toSet()))
                                .orElse(new HashSet<>()))
                    .build();
            });
    }

    private void mockGetAuthorModel(MockedStatic<AuthorFactory> factoryMock) {
        factoryMock.when(() -> AuthorFactory.getAuthorModel(any(Author.class)))
            .thenAnswer(args -> {
                Author author = args.getArgument(0);

                return AuthorModel.builder().id(author.getId()).secondName(author.getSecondName()).firstName(author.getFirstName()).middleName(author.getMiddleName()).build();
            });
    }

    private void testTemplate(Consumer<MockedStatic<AuthorFactory>> action) {
        try (MockedStatic<AuthorFactory> authorFactoryMock = mockStatic(AuthorFactory.class)) {
            action.accept(authorFactoryMock);
        }
    }

    private void checkAuthorDedaultFields(String expectedFirstName, DefaultAuthorFields author) {
        assertEquals(AUTHOR_SECOND_NAME, author.getSecondName());
        assertEquals(expectedFirstName, author.getFirstName());
        assertEquals(AUTHOR_MIDDLE_NAME, author.getMiddleName());
    }

    private Book convertIdToBook(Long id) {
        return Book.builder().id(id).build();
    }
}
