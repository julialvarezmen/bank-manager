package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.AccountRequestDTO;
import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.User;
import com.system.bank_manager.exception.DuplicateAccountException;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceImpl - Tests Esenciales")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    // ==================== TEST 1: createAccount - Caso exitoso ====================
    @Test
    @DisplayName("createAccount - Debería crear cuenta exitosamente")
    void createAccount_Success() {
        // Paso 1: Datos
        AccountRequestDTO request = new AccountRequestDTO("12345", BigDecimal.valueOf(1000), 1L);

        User user = new User();
        user.setId(1L);

        Account saved = new Account();
        saved.setId(10L);
        saved.setAccountNumber("12345");
        saved.setBalance(BigDecimal.valueOf(1000));
        saved.setUser(user);

        // Paso 2: Mocks
        when(accountRepository.existsByAccountNumber("12345")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        // Paso 3: Ejecutar
        AccountResponseDTO result = accountService.createAccount(request);

        // Paso 4: Verificar
        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("12345", result.accountNumber());
        assertEquals(BigDecimal.valueOf(1000), result.balance());
        assertEquals(1L, result.userId());

        // Paso 5: Verificar interacciones
        verify(accountRepository).existsByAccountNumber("12345");
        verify(userRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    // ==================== TEST 2: createAccount - Caso malo (duplicado) ====================
    @Test
    @DisplayName("createAccount - Debería lanzar excepción si el número de cuenta ya existe")
    void createAccount_DuplicateAccount() {
        // Paso 1: Datos
        AccountRequestDTO request = new AccountRequestDTO("12345", BigDecimal.valueOf(500), 1L);

        // Paso 2: Mocks
        when(accountRepository.existsByAccountNumber("12345")).thenReturn(true);

        // Paso 3: Ejecutar y verificar excepción
        assertThrows(DuplicateAccountException.class, () -> accountService.createAccount(request));

        // Paso 4: Verificar interacciones
        verify(accountRepository).existsByAccountNumber("12345");
        verify(userRepository, never()).findById(any());
    }

    // ==================== TEST 3: updateAccount - Caso exitoso ====================
    @Test
    @DisplayName("updateAccount - Debería actualizar cuenta exitosamente")
    void updateAccount_Success() {
        // Paso 1: Datos
        Long id = 1L;
        AccountRequestDTO request = new AccountRequestDTO("54321", BigDecimal.valueOf(2000), 2L);

        User user = new User();
        user.setId(2L);

        Account account = new Account();
        account.setId(id);
        account.setAccountNumber("11111");
        account.setBalance(BigDecimal.valueOf(500));

        Account updated = new Account();
        updated.setId(id);
        updated.setAccountNumber("54321");
        updated.setBalance(BigDecimal.valueOf(2000));
        updated.setUser(user);

        // Paso 2: Mocks
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(updated);

        // Paso 3: Ejecutar
        AccountResponseDTO result = accountService.updateAccount(id, request);

        // Paso 4: Verificar
        assertNotNull(result);
        assertEquals("54321", result.accountNumber());
        assertEquals(BigDecimal.valueOf(2000), result.balance());
        assertEquals(2L, result.userId());

        // Paso 5: Verificar interacciones
        verify(accountRepository).findById(id);
        verify(userRepository).findById(2L);
        verify(accountRepository).save(account);
    }

    // ==================== TEST 4: updateAccount - Caso malo (cuenta no encontrada) ====================
    @Test
    @DisplayName("updateAccount - Debería lanzar excepción si cuenta no existe")
    void updateAccount_AccountNotFound() {
        // Paso 1
        Long id = 99L;
        AccountRequestDTO request = new AccountRequestDTO("54321", BigDecimal.valueOf(2000), 1L);

        // Paso 2
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // Paso 3 y 4
        assertThrows(RuntimeException.class, () -> accountService.updateAccount(id, request));

        // Paso 5
        verify(accountRepository).findById(id);
        verify(userRepository, never()).findById(any());
    }

    // ==================== TEST 5: getAllAccounts - Con resultados ====================
    @Test
    @DisplayName("getAllAccounts - Debería retornar lista de cuentas")
    void getAllAccounts_WithResults() {
        // Paso 1
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);

        Account a1 = new Account();
        a1.setId(1L);
        a1.setAccountNumber("11111");
        a1.setBalance(BigDecimal.valueOf(100));
        a1.setUser(u1);

        Account a2 = new Account();
        a2.setId(2L);
        a2.setAccountNumber("22222");
        a2.setBalance(BigDecimal.valueOf(200));
        a2.setUser(u2);

        List<Account> accounts = Arrays.asList(a1, a2);

        // Paso 2
        when(accountRepository.findAll()).thenReturn(accounts);

        // Paso 3
        List<AccountResponseDTO> result = accountService.getAllAccounts();

        // Paso 4
        assertEquals(2, result.size());
        assertEquals("11111", result.get(0).accountNumber());
        assertEquals("22222", result.get(1).accountNumber());

        // Paso 5
        verify(accountRepository).findAll();
    }

    // ==================== TEST 6: getAllAccounts - Lista vacía ====================
    @Test
    @DisplayName("getAllAccounts - Debería retornar lista vacía")
    void getAllAccounts_Empty() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        List<AccountResponseDTO> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountRepository).findAll();
    }

    // ==================== TEST 7: getAccountById - Caso exitoso ====================
    @Test
    @DisplayName("getAccountById - Debería retornar cuenta exitosamente")
    void getAccountById_Success() {
        Long id = 1L;

        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setId(id);
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(300));
        account.setUser(user);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        AccountResponseDTO result = accountService.getAccountById(id);

        assertNotNull(result);
        assertEquals("12345", result.accountNumber());
        assertEquals(BigDecimal.valueOf(300), result.balance());

        verify(accountRepository).findById(id);
    }

    // ==================== TEST 8: getAccountById - Caso malo ====================
    @Test
    @DisplayName("getAccountById - Debería lanzar excepción si cuenta no existe")
    void getAccountById_NotFound() {
        Long id = 99L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.getAccountById(id));

        verify(accountRepository).findById(id);
    }

    // ==================== TEST 9: deleteAccount - Caso exitoso ====================
    @Test
    @DisplayName("deleteAccount - Debería eliminar cuenta exitosamente")
    void deleteAccount_Success() {
        Long id = 1L;

        doNothing().when(accountRepository).deleteById(id);

        accountService.deleteAccount(id);

        verify(accountRepository).deleteById(id);
    }
}
