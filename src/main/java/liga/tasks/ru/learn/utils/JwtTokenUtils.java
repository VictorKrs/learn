package liga.tasks.ru.learn.utils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import liga.tasks.ru.learn.dto.jwt.TokenInfo;

@Component
public class JwtTokenUtils {

    private final String SECRET_KEY = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
    private final String ROLES_KEY = "roles";

    @Value("${jwt.lifetime:60m}")
    private Duration lifetime;

    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES_KEY, userDetails.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList()));

        Date issueDate = new Date();

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issueDate)
                .expiration(new Date(issueDate.getTime() + lifetime.toMillis()))
                .signWith(getSignKey())
                .compact();
    }

    @SuppressWarnings("unchecked")
    public TokenInfo getTokenInfo(String token) {
        Claims claims = getAllClaimsFromToken(token);

        return new TokenInfo(claims.getSubject(), claims.get(ROLES_KEY, List.class));
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
