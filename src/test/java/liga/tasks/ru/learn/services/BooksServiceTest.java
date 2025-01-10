package liga.tasks.ru.learn.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.exceptions.AuthorNotFoundException;
import liga.tasks.ru.learn.exceptions.BookAlreadyExistException;
import liga.tasks.ru.learn.exceptions.BookNotFoundException;
import liga.tasks.ru.learn.models.book.BookCreate;
import liga.tasks.ru.learn.models.book.BookModel;
import liga.tasks.ru.learn.repositories.AuthorRepository;
import liga.tasks.ru.learn.repositories.BookRepository;

@SpringBootTest(classes = BooksService.class)
public class BooksServiceTest {
    private final Long ID = 1L;
    private final String TITLE = "title";
    private final BookCreate BOOK_CREATE = BookCreate.builder().title(TITLE).build();
    private final BookCreate BOOK_CREATE_WITH_AUTHORS = BookCreate.builder().title(TITLE).authors(new HashSet<Long>() {{ add(10L); }}).build();
    private final Book BOOK = Book.builder().title(TITLE).id(ID).authors(new HashSet<>()).build();


    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    
    @Autowired
    private BooksService booksService;

    @Test
    public void saveTest_authorsNotExist_AuthorNotFoundException() {
        when(authorRepository.findAllById(anyIterable())).thenReturn(new ArrayList<>());

        assertThrows(AuthorNotFoundException.class, () -> booksService.save(BOOK_CREATE_WITH_AUTHORS));
    }

    @Test
    public void saveTest_bookAlreadyExist_BookAlreadyExistException() {
        when(bookRepository.findByTitle(TITLE)).thenReturn(Optional.of(BOOK));

        assertThrows(BookAlreadyExistException.class, () -> booksService.save(BOOK_CREATE));
    }

    @Test
    public void saveTest_good() {
        when(authorRepository.findAllById(anyIterable())).thenAnswer(args -> { 
            HashSet<Long> ids = args.getArgument(0);
            return ids.stream().map(id -> Author.builder().id(id).build()).collect(Collectors.toList());
        });
        when(bookRepository.findByTitle(TITLE)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(BOOK);
        when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));

        BookModel result = booksService.save(BOOK_CREATE_WITH_AUTHORS);
        
        checkResult(BOOK, result);
    }

    @Test
    public void findByIdTest_notFound_BookNotFoundException() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> booksService.findById(ID));
    }

    @Test
    public void findByIdTest_good() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));

        BookModel result = booksService.findById(ID);

        checkResult(BOOK, result);
    }

    @Test
    public void updateTest_bookAlreadyExist_BookAlreadyExistException() {
        String oldTitle = "oldTitle";
        Long bookId = ID + 1;
        Book bookInDb = Book.builder().id(bookId).title(oldTitle).authors(new HashSet<>()).build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookInDb));
        when(bookRepository.findByTitle(TITLE)).thenReturn(Optional.of(BOOK));

        assertThrows(BookAlreadyExistException.class, () -> booksService.update(BookModel.builder().id(bookId).title(TITLE).build()));
    }

    @Test
    public void updateTest_good() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        when(bookRepository.save(any(Book.class))).thenReturn(BOOK);

        BookModel data = BookModel.builder().id(ID).title(TITLE).build();
        BookModel result = booksService.update(data);

        checkResult(BOOK, result);
    }

    @Test
    public void deleteByIdTest_good() {
        booksService.deleteById(ID);

        verify(bookRepository).deleteById(ID);
    }

    private void checkResult(Book source, BookModel result){
        assertNotNull(result);
        assertEquals(BOOK.getId(), result.getId());
        assertEquals(BOOK.getTitle(), result.getTitle());
    }
}
