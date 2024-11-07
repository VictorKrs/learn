package liga.tasks.ru.learn.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import liga.tasks.ru.learn.models.AuthorModel;
import liga.tasks.ru.learn.services.AuthorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorsService authorsService;

    @PostMapping
    public AuthorModel puthAuthor(@RequestBody AuthorModel authorModel) {
        return authorsService.save(authorModel);
    }

    @GetMapping("/{id}")
    public AuthorModel getAuthorById(@PathVariable Long id) {
        return authorsService.findById(id);
    }
    
}
