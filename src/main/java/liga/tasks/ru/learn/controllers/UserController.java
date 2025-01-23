package liga.tasks.ru.learn.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import liga.tasks.ru.learn.dto.users.UserLogin;
import liga.tasks.ru.learn.dto.users.UserRegistration;
import liga.tasks.ru.learn.services.UserService;
import lombok.RequiredArgsConstructor;
import liga.tasks.ru.learn.dto.auth.TokenResponse;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public TokenResponse login(@RequestBody UserLogin userCreds) {
        return userService.login(userCreds);
    }

    @PostMapping()
    public TokenResponse registration(@RequestBody UserRegistration userInfo) {
        return userService.registration(userInfo);
    }
}
