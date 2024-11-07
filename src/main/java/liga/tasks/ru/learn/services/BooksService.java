package liga.tasks.ru.learn.services;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.functions.BookFactory;
import liga.tasks.ru.learn.models.BookModel;
import liga.tasks.ru.learn.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BooksService {

    private final BookRepository bookRepository;


    public List<BookModel> findAll() {
        return bookRepository.findAll().stream()
            .map(BookFactory::creatBookModel)
            .collect(Collectors.toList());
    }

    public BookModel add(BookModel book) {
        return BookFactory.creatBookModel(bookRepository.save(BookFactory.createBook(book)));
    }
 
    public BookModel getBookById(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle("Test Book");
        Author author = new Author();
        author.setId(1L);
        author.setName("Author name");
        author.setBooks(new HashSet<>());
        book.setAuthors(new HashSet<Author>() {{add(author); }});
        
        return BookFactory.creatBookModel(book);
    }
}
