package liga.tasks.ru.learn.services;

import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.dto.auth.TokenResponse;
import liga.tasks.ru.learn.dto.users.UserInfo;
import liga.tasks.ru.learn.dto.users.UserLogin;
import liga.tasks.ru.learn.dto.users.UserRegistration;
import liga.tasks.ru.learn.entities.User;
import liga.tasks.ru.learn.exceptions.auth.WrongUserCredentialsException;
import liga.tasks.ru.learn.exceptions.user.PasswordsNotEqualsRegistrationException;
import liga.tasks.ru.learn.exceptions.user.UserAlreadyExistException;
import liga.tasks.ru.learn.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils tokenUtils;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(UserLogin userCreds) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCreds.getUsername(), userCreds.getPassword()));
        } catch (AuthenticationException e) {
            log.error(e);
            throw new WrongUserCredentialsException();
        }
        UserDetails userDetails = userService.loadUserByUsername(userCreds.getUsername());

        return new TokenResponse(tokenUtils.generateJwtToken(userDetails));
    }

    public UserInfo registration(UserRegistration userReg) {
        if (!Objects.equals(userReg.getPassword(), userReg.getConfirmPassword())) {
            throw new PasswordsNotEqualsRegistrationException();
        }

        if (userService.findByUsername(userReg.getUsername()).isPresent()) {
            throw new UserAlreadyExistException(userReg.getUsername());
        }

        User user = userService.createNewUser(userReg);

        return new UserInfo(user.getId(), user.getUsername());
    }
}
