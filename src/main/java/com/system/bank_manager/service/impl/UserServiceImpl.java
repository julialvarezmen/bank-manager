package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.request.UpdateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;
import com.system.bank_manager.entity.User;
import com.system.bank_manager.exception.DuplicateUserException;
import com.system.bank_manager.mapper.UserMapper;
import com.system.bank_manager.repository.UserRepository;
import com.system.bank_manager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public UserResponseDTO createUser(CreateUserDTO dto) {
        //  Validar DNI duplicado
        if (userRepository.findByDni(dto.dni()).isPresent()) {
            throw new DuplicateUserException("Ya existe un usuario con DNI: " + dto.dni());
        }

        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user); //
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional //
    public UserResponseDTO updateUser(Long id, UpdateUserDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
