package liga.tasks.ru.learn.services;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.exceptions.AuthorAlreadyExistException;
import liga.tasks.ru.learn.exceptions.AuthorNotFoundException;
import liga.tasks.ru.learn.functions.AuthorFactory;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorsService {

    private final AuthorRepository authorRepository;

    public AuthorModel save(AuthorCreate authorIn) {
        Author author = AuthorFactory.getAuthor(authorIn);
        
        checkAuthorOnExist(author);

        return AuthorFactory.getAuthorModel(authorRepository.save(author));
    }

    public AuthorModel findById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorFactory::getAuthorModel)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public AuthorModel update(AuthorModel authorModel) {
        var authorInDb = findById(authorModel.getId());

        Author authorOnUpdate = AuthorFactory.getAuthor(authorModel);
        if (!authorInDb.getName().equals(authorOnUpdate.getName())){
            checkAuthorOnExist(authorOnUpdate);
        }

        return AuthorFactory.getAuthorModel(authorRepository.save(authorOnUpdate));
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    private void checkAuthorOnExist(Author author) {
        authorRepository.findByName(author.getName())
            .ifPresent(authorInDb -> { throw new AuthorAlreadyExistException(authorInDb); });
    }
}
