package liga.tasks.ru.learn.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import liga.tasks.ru.learn.dto.auth.TokenResponse;
import liga.tasks.ru.learn.dto.users.UserInfo;
import liga.tasks.ru.learn.dto.users.UserLogin;
import liga.tasks.ru.learn.dto.users.UserRegistration;
import liga.tasks.ru.learn.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Tag(name="Авторизация")
@Log4j2
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "200", description = "Успешная регистрация пользователя", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserInfo.class)))
    // @ApiResponse(responseCode = "400", description = "Ошибка регистрации: пароли не совпадают", content = @Content(examples = {@ExampleObject("Пароли не совпадают")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "400", description = "Ошибка регистрации: несовпадение паролей, либо пользователь уже существует", content = { @Content(examples = {@ExampleObject("Пользователь с именем \"admin\" уже существует")}, mediaType = MediaType.TEXT_PLAIN_VALUE),
                                                                                                                                                @Content(examples = {@ExampleObject("Пароли не совпадают")}, mediaType = MediaType.TEXT_PLAIN_VALUE)})
    public UserInfo createUser(@RequestBody UserRegistration userRegistration) {
        return authService.registration(userRegistration);
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    @ApiResponse(responseCode = "200", description = "Успешная авторизация пользователя", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "400", description = "Ошибка авторизации: неверная пара логин/пароль", content = @Content(examples = {@ExampleObject("Неверные логин или пароль")}, mediaType = MediaType.TEXT_PLAIN_VALUE))
    public TokenResponse login(@RequestBody UserLogin user) {
        return authService.login(user);
    }
}
