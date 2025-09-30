package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.request.UpdateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;
import com.system.bank_manager.entity.User;
import com.system.bank_manager.mapper.UserMapper;
import com.system.bank_manager.repository.UserRepository;
import com.system.bank_manager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDTO createUser(CreateUserDTO dto) {
        if (userRepository.findByDni(dto.dni()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con este DNI");
        }

        User user = userMapper.toEntity(dto);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }


    @Override
    public UserResponseDTO updateUser(Long id, UpdateUserDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encotrado con ID: " + id));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encotrado con ID: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encotrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
