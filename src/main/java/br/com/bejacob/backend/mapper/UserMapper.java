package br.com.bejacob.backend.mapper;

import br.com.bejacob.backend.dto.auth.RegisterUserRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserResponse;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.entity.enums.UserRoles;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterUserRequest data) {
        return new User(
                data.name(),
                data.email(),
                data.password(),
                UserRoles.CLIENT,
                data.phone()
        );
    }

    public RegisterUserResponse toResponse(User user) {
        return new RegisterUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
