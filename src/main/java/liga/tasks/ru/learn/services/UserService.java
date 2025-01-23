package liga.tasks.ru.learn.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import liga.tasks.ru.learn.dto.auth.TokenResponse;
import liga.tasks.ru.learn.dto.users.UserLogin;
import liga.tasks.ru.learn.dto.users.UserRegistration;
import liga.tasks.ru.learn.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public TokenResponse login(UserLogin userCreds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    public TokenResponse registration(UserRegistration userInfo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registration'");
    }

}
