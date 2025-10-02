package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.request.UpdateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;
import com.system.bank_manager.entity.User;
import com.system.bank_manager.exception.DuplicateUserException;
import com.system.bank_manager.mapper.UserMapper;
import com.system.bank_manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl - Tests Esenciales")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    // TEST 1: createUser - Caso exitoso

    @Test
    @DisplayName("createUser - Debería crear usuario exitosamente")
    void createUser_Success() {
        // PASO 1: Datos de entrada y salida esperados
        CreateUserDTO inputDTO = new CreateUserDTO(
                "12345678990",
                "Julian",
                "julian@mail.com",
                "Password123!"
        );

        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setDni("12345678990");
        userEntity.setName("Julian");
        userEntity.setEmail("julian@mail.com");

        User savedUserEntity = new User();
        savedUserEntity.setId(1L);
        savedUserEntity.setDni("12345678990");
        savedUserEntity.setName("Juan Pérez");
        savedUserEntity.setEmail("juan@email.com");

        UserResponseDTO expectedOutput = new UserResponseDTO(
                1L,
                "12345678990",
                "Juan Pérez",
                "juan@email.com"
        );

        // PASO 2: Establecer comportamientos simulados
        when(userRepository.findByDni(inputDTO.dni())).thenReturn(Optional.empty());
        when(userMapper.toEntity(inputDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.toResponse(savedUserEntity)).thenReturn(expectedOutput);

        // PASO 3: Llamar al método a probar
        UserResponseDTO actualResult = userService.createUser(inputDTO);

        // PASO 4: Verificar los resultados
        assertNotNull(actualResult);
        assertEquals(1L, actualResult.id());
        assertEquals("Juan Pérez", actualResult.name());
        assertEquals("juan@email.com", actualResult.email());
        assertEquals("12345678990", actualResult.dni());

        // PASO 5: Verificar interacciones
        verify(userRepository, times(1)).findByDni(inputDTO.dni());
        verify(userMapper, times(1)).toEntity(inputDTO);
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toResponse(savedUserEntity);
    }

    //  TEST 2: createUser - DNI duplicado

    @Test
    @DisplayName("createUser - Debería lanzar DuplicateUserException cuando DNI ya existe")
    void createUser_ThrowsException_WhenDniExists() {
        // PASO 1: Datos de entrada
        CreateUserDTO inputDTO = new CreateUserDTO(
                "12345678990",
                "Juan Pérez",
                "juan@email.com",
                "Password123!"
        );

        User existingUser = new User();
        existingUser.setId(99L);
        existingUser.setDni("12345678990");

        // PASO 2: Establecer comportamientos simulados
        when(userRepository.findByDni(inputDTO.dni())).thenReturn(Optional.of(existingUser));

        // PASO 3: Ejecutar y capturar excepción
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> userService.createUser(inputDTO)
        );

        // PASO 4: Verificar mensaje correcto
        assertEquals("Ya existe un usuario con DNI: " + inputDTO.dni(), exception.getMessage());

        // PASO 5: Verificar interacciones
        verify(userRepository, times(1)).findByDni(inputDTO.dni());
        verify(userMapper, never()).toEntity(any());
        verify(userRepository, never()).save(any());
    }

    // TEST 3: updateUser - Caso exitoso

    @Test
    @DisplayName("updateUser - Debería actualizar usuario exitosamente")
    void updateUser_Success() {
        Long userId = 1L;

        UpdateUserDTO inputDTO = new UpdateUserDTO(
                "Juan Pérez Actualizado",
                "juan.nuevo@email.com",
                "NewPassword123!"
        );

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Juan Pérez");
        existingUser.setEmail("juan@email.com");
        existingUser.setDni("12345678990");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Juan Pérez Actualizado");
        updatedUser.setEmail("juan.nuevo@email.com");
        updatedUser.setDni("12345678990");

        UserResponseDTO expectedOutput = new UserResponseDTO(
                userId,
                "12345678990",
                "Juan Pérez Actualizado",
                "juan.nuevo@email.com"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(expectedOutput);

        UserResponseDTO actualResult = userService.updateUser(userId, inputDTO);

        assertNotNull(actualResult);
        assertEquals(userId, actualResult.id());
        assertEquals("Juan Pérez Actualizado", actualResult.name());
        assertEquals("juan.nuevo@email.com", actualResult.email());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toResponse(updatedUser);
    }

    //  TEST 4: updateUser - Usuario no encontrado

    @Test
    @DisplayName("updateUser - Debería lanzar EntityNotFoundException cuando usuario no existe")
    void updateUser_ThrowsException_WhenUserNotFound() {
        Long userId = 999L;
        UpdateUserDTO inputDTO = new UpdateUserDTO(
                "Juan Pérez",
                "juan@email.com",
                "Password123!"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUser(userId, inputDTO)
        );

        assertTrue(exception.getMessage().contains("Usuario no encontrado con ID: " + userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    // TEST 5: getAllUsers - Con usuarios

    @Test
    @DisplayName("getAllUsers - Debería retornar lista de usuarios exitosamente")
    void getAllUsers_Success() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Juan Pérez");
        user1.setEmail("juan@email.com");
        user1.setDni("12345678");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("María García");
        user2.setEmail("maria@email.com");
        user2.setDni("87654321");

        List<User> usersList = Arrays.asList(user1, user2);

        UserResponseDTO response1 = new UserResponseDTO(1L, "12345678", "Juan Pérez", "juan@email.com");
        UserResponseDTO response2 = new UserResponseDTO(2L, "87654321", "María García", "maria@email.com");

        when(userRepository.findAll()).thenReturn(usersList);
        when(userMapper.toResponse(user1)).thenReturn(response1);
        when(userMapper.toResponse(user2)).thenReturn(response2);

        List<UserResponseDTO> actualResult = userService.getAllUsers();

        assertNotNull(actualResult);
        assertEquals(2, actualResult.size());
        assertEquals("Juan Pérez", actualResult.get(0).name());
        assertEquals("María García", actualResult.get(1).name());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).toResponse(any(User.class));
    }

    // TEST 6: getAllUsers - Lista vacía

    @Test
    @DisplayName("getAllUsers - Debería retornar lista vacía cuando no hay usuarios")
    void getAllUsers_ReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> actualResult = userService.getAllUsers();

        assertNotNull(actualResult);
        assertTrue(actualResult.isEmpty());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, never()).toResponse(any());
    }

    // TEST 7: getUserById - Caso exitoso

    @Test
    @DisplayName("getUserById - Debería retornar usuario por ID exitosamente")
    void getUserById_Success() {
        Long userId = 1L;

        User userEntity = new User();
        userEntity.setId(userId);
        userEntity.setName("Juan Pérez");
        userEntity.setEmail("juan@email.com");
        userEntity.setDni("12345678");

        UserResponseDTO expectedOutput = new UserResponseDTO(
                userId,
                "12345678",
                "Juan Pérez",
                "juan@email.com"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(expectedOutput);

        UserResponseDTO actualResult = userService.getUserById(userId);

        assertNotNull(actualResult);
        assertEquals(userId, actualResult.id());
        assertEquals("Juan Pérez", actualResult.name());
        assertEquals("juan@email.com", actualResult.email());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toResponse(userEntity);
    }

    //TEST 8: getUserById - Usuario no encontrado

    @Test
    @DisplayName("getUserById - Debería lanzar EntityNotFoundException cuando usuario no existe")
    void getUserById_ThrowsException_WhenUserNotFound() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        assertTrue(exception.getMessage().contains("Usuario no encontrado con ID: " + userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).toResponse(any());
    }

    //TEST 9: deleteUser - Caso exitoso

    @Test
    @DisplayName("deleteUser - Debería eliminar usuario exitosamente")
    void deleteUser_Success() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> userService.deleteUser(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    //  TEST 10: deleteUser - Usuario no encontrado

    @Test
    @DisplayName("deleteUser - Debería lanzar EntityNotFoundException cuando usuario no existe")
    void deleteUser_ThrowsException_WhenUserNotFound() {
        Long userId = 999L;

        when(userRepository.existsById(userId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        assertTrue(exception.getMessage().contains("Usuario no encontrado con ID: " + userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}
