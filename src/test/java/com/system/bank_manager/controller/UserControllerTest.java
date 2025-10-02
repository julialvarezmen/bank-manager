package com.system.bank_manager.controller;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;
import com.system.bank_manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_whenValidRequest_returnsCreatedUser() {
        // 1. Datos de entrada
        CreateUserDTO request = new CreateUserDTO("12345678", "Julian", "julian@test.com", "password123");
        UserResponseDTO expectedResponse = new UserResponseDTO(1L, "12345678", "Julian", "julian@test.com");

        // 2. Definir comportamiento del mock
        when(userService.createUser(request)).thenReturn(expectedResponse);

        // 3. Llamar al método del controlador
        ResponseEntity<UserResponseDTO> response = userController.createUser(request);

        // 4. Verificar resultados
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        // 5. Verificar interacción con mocks
        verify(userService).createUser(request);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void createUser_whenDniAlreadyExists_throwsException() {
        // 1. Datos de entrada
        CreateUserDTO request = new CreateUserDTO("12345678", "Julian", "julian@test.com", "password123");

        // 2. Definir comportamiento del mock -> simula excepción
        when(userService.createUser(request)).thenThrow(new IllegalArgumentException("Ya existe un usuario con este DNI"));

        // 3. Llamar al método y capturar excepción
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userController.createUser(request));

        // 4. Verificar mensaje de error
        assertEquals("Ya existe un usuario con este DNI", exception.getMessage());

        // 5. Verificar interacción con mocks
        verify(userService).createUser(request);
        verifyNoMoreInteractions(userService);
    }
}
