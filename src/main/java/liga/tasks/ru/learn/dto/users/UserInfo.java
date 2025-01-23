package liga.tasks.ru.learn.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Инормация о пользователе")
@Data
public class UserInfo {
    private Long id;
    private String username;
}
