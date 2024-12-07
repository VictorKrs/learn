package liga.tasks.ru.learn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import liga.tasks.ru.learn.models.book.BookCreate;
import liga.tasks.ru.learn.models.book.BookModel;
import liga.tasks.ru.learn.services.BooksService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;


    @PostMapping
    public BookModel addBook(@RequestBody BookCreate book) {
        return booksService.save(book);
    }

    @GetMapping("/{id}")
    public BookModel getBookById(@PathVariable Long id) {
        return booksService.findById(id);
    }

    @PutMapping
    public BookModel updateBook(@RequestBody BookModel book) {
        return booksService.update(book);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteById(@PathParam("id") Long id) {
        booksService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
