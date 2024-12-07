package liga.tasks.ru.learn.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import liga.tasks.ru.learn.models.book.BookModel;
import liga.tasks.ru.learn.services.BooksService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;

    @GetMapping("/all")
    @ResponseBody
    public List<BookModel> getAll() {
        return booksService.findAll();
    }

    @PutMapping
    @ResponseBody
    public BookModel put(@RequestBody BookModel book) {
        return booksService.add(book);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public BookModel getBook(@PathVariable Long id) {
        return booksService.getBookById(id);
    }
}
