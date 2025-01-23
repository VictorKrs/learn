package liga.tasks.ru.learn.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные пользователя для регистрации")
public class UserRegistration {
    private String username;
    private String password;
}
