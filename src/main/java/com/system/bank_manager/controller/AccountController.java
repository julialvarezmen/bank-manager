package com.system.bank_manager.controller;

import com.system.bank_manager.dto.request.AccountRequestDTO;
import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name="Cuentas", description="Operaciones relacionadas con Cuentas")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Crear cuenta
    @PostMapping
    @Operation(summary = "Crear una cuenta nueva")
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    // Actualizar cuenta
    @PutMapping("/{id}")
    @Operation(summary = "Editar cuenta")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequestDTO request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    // Listar cuentas
    @GetMapping
    @Operation(summary = "Lista todas las cuentas")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // Obtener cuenta por id
    @GetMapping("/{id}")
    @Operation(summary = "Busca cuenta por ID")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    // Eliminar cuenta
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cuenta")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}

