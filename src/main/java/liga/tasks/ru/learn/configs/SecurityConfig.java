package liga.tasks.ru.learn.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import liga.tasks.ru.learn.services.UserService;
import liga.tasks.ru.learn.consts.Roles;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtRequestFilter requestFilter;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(crfs -> crfs.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(authHttp -> {
                authHttp.requestMatchers(HttpMethod.POST, "/authors", "/books").hasRole(Roles.USER_ROLE_CONF)
                        .requestMatchers(HttpMethod.PUT, "/authors", "/books").hasRole(Roles.ADMIN_ROLE_CONF)
                        .requestMatchers(HttpMethod.DELETE,"/authors", "/books").hasRole(Roles.ADMIN_ROLE_CONF)
                        .anyRequest().permitAll();
            })
            .sessionManagement(sessionM -> {
                sessionM.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .exceptionHandling(excH -> {
                excH.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
            })
            .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider result = new DaoAuthenticationProvider();
        
        result.setPasswordEncoder(passwordEncoder);
        result.setUserDetailsService(userService);

        return result;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
