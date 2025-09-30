package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.User;
import com.system.bank_manager.mapper.AccountMapper;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.service.impl.AccountServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccountById_whenAccountExists_returnsAccount() {
        // 1. Datos de entrada
        Long accountId = 1L;
        User user = new User();
        user.setId(1L);
        user.setName("Julian");

        Account account = new Account();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        account.setBalance(BigDecimal.valueOf(1000));
        account.setUser(user);


        AccountResponseDTO response = new AccountResponseDTO(
                accountId,
                "123456789",
                BigDecimal.valueOf(1000),
                1L
        );


        // 2. Comportamientos simulados
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Aquí no usas un mapper, sino que llamao a mapToResponseDTO dentro del service,
        // así que el mock de mapper no es necesario.

        // 3. Llamar al metodo
        AccountResponseDTO result = accountService.getAccountById(accountId);

        // 4. Verificar resultados
        assertNotNull(result);
        assertEquals("123456789", result.accountNumber());

        // 5. Verificar interacciones
        verify(accountRepository).findById(accountId);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void getAccountById_whenAccountNotFound_throwsException() {
        // 1. Datos de entrada
        Long accountId = 1L;

        // 2. Comportamientos simulados
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // 3 y 4. Ejecutar y verificar excepción
        assertThrows(EntityNotFoundException.class, () -> accountService.getAccountById(accountId));

        // 5. Verificar interacciones
        verify(accountRepository).findById(accountId);
        verifyNoMoreInteractions(accountRepository);
    }
}
