package liga.tasks.ru.learn.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для авторизации")
public class UserLogin {

    private String username;
    private String password;
}
