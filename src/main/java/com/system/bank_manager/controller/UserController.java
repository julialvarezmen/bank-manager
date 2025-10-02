package com.system.bank_manager.controller;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.request.UpdateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;
import com.system.bank_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con Usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @Operation(summary = "Crear un nuevo usuario")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO request) {
        return ResponseEntity.ok(userService.createUser(request));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario con: {id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserDTO request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }


    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario con: {id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario con: {id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

