package liga.tasks.ru.learn.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.exceptions.AuthorAlreadyExistException;
import liga.tasks.ru.learn.exceptions.AuthorNotFoundException;
import liga.tasks.ru.learn.exceptions.BookNotFoundException;
import liga.tasks.ru.learn.functions.AuthorFactory;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.repositories.AuthorRepository;
import liga.tasks.ru.learn.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorsService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorModel save(AuthorCreate authorIn) {
        checkBooksOnExist(authorIn.getBooks());
        
        Author author = AuthorFactory.getAuthor(authorIn);
        
        checkAuthorOnExist(author);

        return findById(authorRepository.save(author).getId());
    }

    public AuthorModel findById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorFactory::getAuthorModel)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public AuthorModel update(AuthorModel authorModel) {
        var authorInDb = findById(authorModel.getId());

        Author authorOnUpdate = AuthorFactory.getAuthor(authorModel);
        if (!authorInDb.getFullName().equals(authorOnUpdate.getFullName())){
            checkAuthorOnExist(authorOnUpdate);
        }

        return AuthorFactory.getAuthorModel(authorRepository.save(authorOnUpdate));
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    private void checkBooksOnExist(Set<Long> booksId) {
        if (booksId == null || booksId.size() == 0) {
            return;
        }

        Set<Long> booksIdFromDb = bookRepository.findAllById(booksId).stream().map(Book::getId).collect(Collectors.toSet());

        if (booksId.size() != booksIdFromDb.size()) {
            throw new BookNotFoundException(booksId.stream().filter(id -> !booksIdFromDb.contains(id)).collect(Collectors.toList()));
        }
    }

    private void checkAuthorOnExist(Author author) {
        authorRepository.findBySecondNameAndFirstNameAndMiddleName(author.getSecondName(), author.getFirstName(), author.getMiddleName())
            .ifPresent(authorInDb -> { throw new AuthorAlreadyExistException(authorInDb); });
    }
}
