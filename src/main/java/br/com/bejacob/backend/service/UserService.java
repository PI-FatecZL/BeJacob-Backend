package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.user.UpdateProfileRequest;
import br.com.bejacob.backend.dto.user.UserResponse;
import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.exception.user.UserNotFoundException;
import br.com.bejacob.backend.mapper.UserMapper;
import br.com.bejacob.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;
    private UserMapper mapper;

    public UserService(
            UserRepository repository,
            UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<UserResponse> listAllUsers(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    public UserResponse detailsUser(Long id) {
        User user = repository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return mapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateProfileRequest request) {
        User user = repository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        mapper.updateEntityFromDto(request, user);
        return mapper.toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        user.softDelete();
        repository.save(user);
    }
}
