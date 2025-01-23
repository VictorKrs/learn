package liga.tasks.ru.learn.utils;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {

    @Value("${jwt.lifetime:60}")
    private Duration lifetime;

    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList()));

        Date issueDate = new Date();

        return "";
    }
}
