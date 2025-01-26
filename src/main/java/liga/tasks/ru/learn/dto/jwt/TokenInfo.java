package liga.tasks.ru.learn.dto.jwt;

import java.util.List;

public record TokenInfo(String username, List<String> roles) { }
