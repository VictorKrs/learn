package liga.tasks.ru.learn.configs;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liga.tasks.ru.learn.dto.jwt.TokenInfo;
import liga.tasks.ru.learn.exceptions.auth.ErrorTokenException;
import liga.tasks.ru.learn.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        TokenInfo tokenInfo = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            String jwt = authHeader.substring(7);

            try {
                tokenInfo = jwtUtils.getTokenInfo(jwt);
            } catch (Exception e) {
                log.error(e);
                throw new ErrorTokenException();
            }
        }
        if (tokenInfo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                tokenInfo.username(),
                null,
                tokenInfo.roles().stream().map(SimpleGrantedAuthority::new).toList()
            );

            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
