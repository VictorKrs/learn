package liga.tasks.ru.learn.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.dto.book.BookCreate;
import liga.tasks.ru.learn.dto.book.BookModel;
import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.exceptions.author.AuthorNotFoundException;
import liga.tasks.ru.learn.exceptions.book.BookAlreadyExistException;
import liga.tasks.ru.learn.exceptions.book.BookNotFoundException;
import liga.tasks.ru.learn.functions.BookFactory;
import liga.tasks.ru.learn.repositories.AuthorRepository;
import liga.tasks.ru.learn.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BooksService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    

    public BookModel save(BookCreate bookCreate) {
        checkAuthorsOnExist(bookCreate.getAuthors());

        Book book = BookFactory.getBook(bookCreate);
        
        checkBookOnExist(book);

        return findById(bookRepository.save(book).getId());
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

    private void checkAuthorsOnExist(Set<Long> authorsId) {
        if (authorsId == null || authorsId.size() == 0) {
            return;
        }

        Set<Long> authorsIdFromDb = authorRepository.findAllById(authorsId).stream().map(Author::getId).collect(Collectors.toSet());

        if (authorsId.size() != authorsIdFromDb.size()) {
            throw new AuthorNotFoundException(authorsId.stream().filter(id -> !authorsIdFromDb.contains(id)).collect(Collectors.toList()));
        }
    }
}
