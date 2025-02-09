package liga.tasks.ru.learn.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import liga.tasks.ru.learn.dto.author.AuthorCreate;
import liga.tasks.ru.learn.dto.author.AuthorModel;
import liga.tasks.ru.learn.services.AuthorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Авторы", description = "API для работы с авторами")
public class AuthorsController {

    private final AuthorsService authorsService;

    @PostMapping
    @Operation(summary = "Добавление автора", description = "Сохранение информации об авторе в БД интернет магазина;")
    @SecurityRequirement(name = "AuthAny")
    @ApiResponse(responseCode = "200", description = "Успешное сохранение автора", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorModel.class)))
    @ApiResponse(responseCode = "400", description = "Автор с таким именем уже существует", content = @Content(examples = {@ExampleObject("Автор с именем \"Алан Милн\" уже существует")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "401", description = "Неавторизированный пользователь")
    @ApiResponse(responseCode = "404", description = "Не найдены произведения с указанными id", content = @Content(examples = {@ExampleObject("Не найдены произведения с id: 1, 2, 3")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public AuthorModel addAuthor(@RequestBody AuthorCreate authorModel) {
        return authorsService.save(authorModel);
    }

    

    @GetMapping()
    @Operation(summary = "Поиск авторов", description = "Поиск авторов с фильтрацией по первой букве")
    public List<AuthorModel> getAuthors(@Size(max = 1, message = "from может содержать максимум 1 символ") @RequestParam("from") @Parameter(description = "Стартовая буква для поиска", example = "Г", allowEmptyValue = true) String from,
        @Size(max = 1, message = "to может содержать максимум 1 символ") @RequestParam("to") @Parameter(description = "Конечная буква для поиска", example = "Щ", allowEmptyValue = true) String to) {
        return authorsService.getAuthors(from, to);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об авторе", description = "Получение информации об авторе по id")
    @ApiResponse(responseCode = "200", description = "Успешное получение информации об авторе", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorModel.class)))
    @ApiResponse(responseCode = "404", description = "Автор не найден", content = @Content(examples = {@ExampleObject("Не найден автор с id: 100")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public AuthorModel getAuthorById(@PathVariable(required = true, name="id") Long id) {
        return authorsService.findById(id);
    }

    @PutMapping
    @Operation(summary = "Обновление информации об авторе")
    @SecurityRequirement(name = "AuthAdmin")
    @ApiResponse(responseCode = "200", description = "Успешное обновление информации об авторе", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorModel.class)))
    @ApiResponse(responseCode = "400", description = "Автор с таким именем уже существует", content = @Content(examples = {@ExampleObject("Автор с именем \"Алан Милн\" уже существует")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "401", description = "Неавторизированный пользователь")
    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    @ApiResponse(responseCode = "404", description = "Автор не найден", content = @Content(examples = {@ExampleObject("Не найден автор с id: 100")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public AuthorModel updateAuthor(@RequestBody AuthorModel author) {
        return authorsService.update(author);
    }

    @DeleteMapping
    @Operation(summary = "Удаление информации об авторе", description = "Удаление информации об авторе по id")
    @SecurityRequirement(name = "AuthAdmin")
    @ApiResponse(responseCode = "204", description = "Успешное удаление", content = @Content(examples = {@ExampleObject("NO CONTENT")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "401", description = "Неавторизированный пользователь")
    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    public ResponseEntity<String> deleteById(@RequestParam("id") @Parameter(description = "ID автора", example = "100") Long id) {
        authorsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
