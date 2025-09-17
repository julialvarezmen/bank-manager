package com.system.bank_manager.service;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.request.UpdateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(CreateUserDTO request);
    UserResponseDTO updateUser(Long id, UpdateUserDTO request);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    void deleteUser(Long id);
}
