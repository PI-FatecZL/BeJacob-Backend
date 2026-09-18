package br.com.bejacob.backend.mapper;

import br.com.bejacob.backend.dto.auth.RegisterUserRequest;
import br.com.bejacob.backend.dto.auth.RegisterUserResponse;
import br.com.bejacob.backend.dto.user.UpdateProfileRequest;
import br.com.bejacob.backend.dto.user.UserResponse;
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

    public RegisterUserResponse toResponseRegister(User user) {
        return new RegisterUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public void updateEntityFromDto(UpdateProfileRequest request, User user) {
        if (request.name() != null) user.setName(request.name());
        if (request.email() != null) user.setEmail(request.email());
        if (request.phone() != null) user.setPhone(request.phone());
    }
}
