package ru.markn.webchat.servicies;

import org.springframework.http.ResponseEntity;
import ru.markn.webchat.dtos.JwtRequest;
import ru.markn.webchat.dtos.RegistrationUserDto;

public interface IAuthService {
    ResponseEntity<?> createAuthToken(JwtRequest authRequest);
    ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto);
}
