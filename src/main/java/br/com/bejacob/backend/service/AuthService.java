package br.com.bejacob.backend.service;

import br.com.bejacob.backend.config.security.dto.DataToken;
import br.com.bejacob.backend.config.security.service.TokenService;
import br.com.bejacob.backend.dto.MessageResponse;
import br.com.bejacob.backend.dto.auth.LoginRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserResponse;
import br.com.bejacob.backend.dto.auth.UpdateMeRequest;
import br.com.bejacob.backend.dto.auth.UpdatePasswordRequest;
import br.com.bejacob.backend.dto.user.UserResponse;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.entity.enums.UserRoles;
import br.com.bejacob.backend.exception.auth.EmailAlreadyExistsException;
import br.com.bejacob.backend.exception.auth.InactiveUserException;
import br.com.bejacob.backend.exception.auth.InvalidCredentialsException;
import br.com.bejacob.backend.exception.auth.InvalidCurrentPasswordException;
import br.com.bejacob.backend.exception.auth.PhoneAlreadyExistsException;
import br.com.bejacob.backend.exception.user.UserNotFoundException;
import br.com.bejacob.backend.mapper.UserMapper;
import br.com.bejacob.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
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
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        if (repository.existsByPhone(request.phone())) {
            throw new PhoneAlreadyExistsException("Telefone já cadastrado");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(encoder.encode(request.password()));
        user.setRole(UserRoles.CLIENT);

        User saved = repository.save(user);
        return userMapper.toResponseRegister(saved);
    }

    public DataToken login(LoginRequest data) {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                            data.email(),
                            data.password()
                    );
            Authentication authentication = manager.authenticate(token);
            User user = (User) authentication.getPrincipal();

            if (user == null) {
                throw new UserNotFoundException("User não encontrado");
            }

            if (!user.isActive()) {
                throw new InactiveUserException("Usuário inativo, não é possível realizar o login");
            }

            String tokenJWT = tokenService.generateToken(user);
            return new DataToken(
                    tokenJWT,
                    user.getId(),
                    user.getEmail(),
                    user.getRole()
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("E-mail ou senha inválidos");
        }
    }

    @Transactional
    public MessageResponse updatePassword(UpdatePasswordRequest data, Long id) {
        User user = repository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        if(!encoder.matches(data.oldPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException("Senha Antiga está Inválida");
        }
        if(encoder.matches(data.newPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Nova senha não pode ser igual a senha atual");
        }
        String newPassword = encoder.encode(data.newPassword());
        user.updatePassword(newPassword);
        repository.save(user);
        return new MessageResponse("Nova Senha atualizada");
    }

    public UserResponse getMe(Long id) {
        User user = repository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateMe(UpdateMeRequest data, Long id) {
        User user = repository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        if (data.email() != null && repository.existsByEmailAndIdNot(data.email(), id)) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }
        if (data.phone() != null && repository.existsByPhoneAndIdNot(data.phone(), id)) {
            throw new PhoneAlreadyExistsException("Telefone já cadastrado");
        }
        userMapper.updateEntityFromDto(data, user);
        User saved = repository.save(user);
        return userMapper.toResponse(saved);
    }
}