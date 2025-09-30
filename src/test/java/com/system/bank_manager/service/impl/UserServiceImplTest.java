package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;
import com.system.bank_manager.entity.User;
import com.system.bank_manager.mapper.UserMapper;
import com.system.bank_manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserMapper userMapper = Mockito.mock(UserMapper.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepository, userMapper);

    @Test
    void createUser_whenDniDoesNotExist_createsUser() {
        // 1. Datos de entrada
        CreateUserDTO request = new CreateUserDTO("12345678", "Julian", "juan@email.com", "password");
        User user = new User();
        user.setId(1L);
        user.setDni("12345678");
        user.setName("Juan");
        UserResponseDTO expectedResponse = new UserResponseDTO(1L, "12345678", "Julian", "julian@email.com");


        // 2. Comportamientos simulados
        Mockito.when(userRepository.findByDni("12345678")).thenReturn(Optional.empty());
        Mockito.when(userMapper.toEntity(request)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        // 3. Llamar al método
        UserResponseDTO result = userService.createUser(request);

        // 4. Verificar resultados
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.dni(), result.dni());
        assertEquals(expectedResponse.name(), result.name());
        assertEquals(expectedResponse.email(), result.email());


        // 5. Verificar interacciones con mocks
        verify(userRepository).findByDni("12345678");
        verify(userMapper).toEntity(request);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void createUser_whenDniAlreadyExists_throwsException() {
        // 1. Datos de entrada
        CreateUserDTO request = new CreateUserDTO("12345678", "Julian", "julian@email.com", "password");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setDni("12345678");

        // 2. Comportamientos simulados
        Mockito.when(userRepository.findByDni("12345678")).thenReturn(Optional.of(existingUser));

        // 3. Llamar al método y capturar excepción
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(request));

        // 4. Verificar resultados
        assertEquals("Ya existe un usuario con este DNI", exception.getMessage());

        // 5. Verificar interacciones con mocks
        verify(userRepository).findByDni("12345678");
        verifyNoMoreInteractions(userRepository, userMapper);
    }
}

