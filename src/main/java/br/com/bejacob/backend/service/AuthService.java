package br.com.bejacob.backend.service;

import br.com.bejacob.backend.config.security.dto.DataToken;
import br.com.bejacob.backend.config.security.service.TokenService;
import br.com.bejacob.backend.dto.auth.LoginRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserResponse;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.entity.enums.UserRoles;
import br.com.bejacob.backend.mapper.UserMapper;
import br.com.bejacob.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public AuthService(
            AuthenticationManager manager,
            TokenService tokenService,
            UserRepository repository,
            PasswordEncoder encoder,
            UserMapper userMapper) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.repository = repository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    public RegisterUserResponse register(RegisterUserRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }
        String hashedPassword = encoder.encode(request.password());
        RegisterUserRequest data = new RegisterUserRequest(
                request.name(),
                request.email(),
                hashedPassword,
                request.phone(),
                UserRoles.CLIENT
        );
        User user = userMapper.toEntity(data);
        User saved = repository.save(user);
        return userMapper.toResponse(saved);
    }


    public DataToken login(LoginRequest data) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        data.email(),
                        data.password()
                );
        Authentication authentication = manager.authenticate(token);
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new BadCredentialsException("User não encontrado");
        }
        String tokenJWT = tokenService.generateToken(user);
        return new DataToken(
                tokenJWT,
                user.getId(),
                user.getEmail()
        );
    }
}
