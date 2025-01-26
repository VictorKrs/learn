package liga.tasks.ru.learn.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(
    title = "REST документация сервиса",
    description = "Описание REST API сервиса"
))
@SecurityScheme(
        name = "AuthAny",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Функционал доступен для любого авторизированного пользователя"
)
@SecurityScheme(
        name = "AuthAdmin",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Функционал доступен только для пользователей с правами администратора"
)
public class OpenApiConfig {
}
