package liga.tasks.ru.learn.services;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.exceptions.BookAlreadyExistException;
import liga.tasks.ru.learn.exceptions.BookNotFoundException;
import liga.tasks.ru.learn.functions.BookFactory;
import liga.tasks.ru.learn.models.book.BookCreate;
import liga.tasks.ru.learn.models.book.BookModel;
import liga.tasks.ru.learn.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BooksService {

    private final BookRepository bookRepository;
    

    public BookModel save(BookCreate bookCreate) {
        Book book = BookFactory.getBook(bookCreate);
        
        checkBookOnExist(book);

        return BookFactory.getBookModel(bookRepository.save(book));
    }

    public BookModel findById(Long id) {
        return bookRepository.findById(id)
                .map(BookFactory::getBookModel)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public BookModel update(BookModel bookModel) {
        var bookInDb = findById(bookModel.getId());

        Book bookOnUpdate = BookFactory.getBook(bookModel);
        if (!bookInDb.getTitle().equals(bookOnUpdate.getTitle())){
            checkBookOnExist(bookOnUpdate);
        }

        return BookFactory.getBookModel(bookRepository.save(bookOnUpdate));
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private void checkBookOnExist(Book book) {
        bookRepository.findByTitle(book.getTitle())
            .ifPresent(bookInDb -> { throw new BookAlreadyExistException(bookInDb); });
    }
}
