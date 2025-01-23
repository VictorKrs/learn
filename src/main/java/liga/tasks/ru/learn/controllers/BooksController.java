package liga.tasks.ru.learn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import liga.tasks.ru.learn.dto.book.BookCreate;
import liga.tasks.ru.learn.dto.book.BookModel;
import liga.tasks.ru.learn.services.BooksService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Tag(name = "Произведения", description = "API для работы с произведениями в интернет магазине")
public class BooksController {

    private final BooksService booksService;

    @PostMapping
    @Operation(summary = "Добавление произведения", description = "Сохранение информации о произведении в БД интернет магазина")
    @ApiResponse(responseCode = "200", description = "Успешное добавление произведения", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BookModel.class)))
    @ApiResponse(responseCode = "400", description = "Произведение с таким названием уже существует", content = @Content(examples = {@ExampleObject("Произведение с названием \"Винни-Пух\" уже существует")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "404", description = "Не найдены авторы с указанными id", content = @Content(examples = {@ExampleObject("Не найдены авторы с id: 1, 2, 3")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public BookModel addBook(@RequestBody BookCreate book) {
        return booksService.save(book);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получение произведения", description = "Получение информации о произведении по id")
    @ApiResponse(responseCode = "200", description = "Произведение найдено", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BookModel.class)))
    @ApiResponse(responseCode = "404", description = "Произведение не найдено", content = @Content(examples = {@ExampleObject("Не найдено произведение с id 100")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public BookModel getBookById(@PathVariable(required = true, name="id") @Parameter(description = "ID произведения", example = "100")  Long id) {
        return booksService.findById(id);
    }

    @PutMapping
    @Operation(summary = "Обновление произведения", description = "Обновление информации о произведении в БД интернет магазина")
    @ApiResponse(responseCode = "200", description = "Информация о произведении успешно обновлена", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BookModel.class)))
    @ApiResponse(responseCode = "400", description = "Произведение с таким названием уже существует", content = @Content(examples = {@ExampleObject("Произведение с названием \"Винни-Пух\" уже существует")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "404", description = "Произведение не найдено", content = @Content(examples = {@ExampleObject("Не найдено произведение с id 100")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public BookModel updateBook(@RequestBody BookModel book) {
        return booksService.update(book);
    }

    @DeleteMapping
    @Operation(summary = "Удаление произведения", description = "Удаление информации о произведении из БД по id")
    @ApiResponse(responseCode = "204", description = "Успешное удаление произведения", content = @Content(examples = {@ExampleObject("NO CONTENT")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public ResponseEntity<String> deleteById(@RequestParam("id") @Parameter(description = "ID произведения", example = "100") Long id) {
        booksService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
