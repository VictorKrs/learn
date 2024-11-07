package liga.tasks.ru.learn.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.functions.AuthorFactory;
import liga.tasks.ru.learn.models.AuthorModel;
import liga.tasks.ru.learn.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
// @RequiredArgsConstructor
@Log4j2
public class AuthorsService {

    private final AuthorRepository authorRepository;

    public AuthorsService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
        log.info("test MESWSAGE");
    }

    public List<AuthorModel> findAll() {
        return authorRepository.findAll().stream()
            .map(AuthorModel::createAuthorModel)
            .collect(Collectors.toList());
    }
   
    public AuthorModel findById(Long id) {
        return AuthorFactory.createAuthorModel(authorRepository.findById(id).get());
    }

    public AuthorModel save(AuthorModel author) {
        return AuthorFactory.createAuthorModel(authorRepository.save(AuthorFactory.createAuthor(author)));
    }
}
