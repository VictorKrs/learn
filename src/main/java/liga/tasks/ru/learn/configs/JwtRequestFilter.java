package liga.tasks.ru.learn.configs;

import org.springframework.stereotype.Component;

import liga.tasks.ru.learn.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter {

    private final JwtTokenUtils jwtUtils;

}
