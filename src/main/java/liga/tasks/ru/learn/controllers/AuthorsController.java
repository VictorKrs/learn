package liga.tasks.ru.learn.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.services.AuthorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorsService authorsService;

    @PostMapping
    public AuthorModel addAuthor(@RequestBody AuthorCreate authorModel) {
        return authorsService.save(authorModel);
    }

    @GetMapping("/{id}")
    public AuthorModel getAuthorById(@PathVariable Long id) {
        return authorsService.findById(id);
    }

    @PutMapping
    public AuthorModel putMethodName(@RequestBody AuthorModel author) {
        return authorsService.update(author);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteById(@PathParam("id") Long id) {
        authorsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
