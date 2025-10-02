package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.Transaction;
import com.system.bank_manager.exception.InsufficientFundsException;
import com.system.bank_manager.mapper.TransactionMapper;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionServiceImpl - Tests Esenciales")
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    // TEST 1: createTransaction - Caso exitoso (DEPOSIT)
    @Test
    @DisplayName("createTransaction - Debería crear transacción de depósito exitosamente")
    void createTransaction_Deposit_Success() {
        // PASO 1: Datos de entrada y salida esperados
        Long accountId = 1L;
        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("100.00"),
                Transaction.TransactionType.DEPOSIT,
                accountId
        );

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("500.00"));

        Transaction transactionEntity = new Transaction();
        transactionEntity.setId(10L);
        transactionEntity.setAmount(new BigDecimal("100.00"));
        transactionEntity.setType(Transaction.TransactionType.DEPOSIT);

        TransactionResponseDTO expectedResponse = new TransactionResponseDTO(
                10L,
                new BigDecimal("100.00"),
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now(),
                accountId
        );

        // PASO 2: Configurar mocks
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionMapper.toEntity(request)).thenReturn(transactionEntity);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionEntity);
        when(transactionMapper.toResponse(transactionEntity)).thenReturn(expectedResponse);

        // PASO 3: Llamar al método
        TransactionResponseDTO actualResult = transactionService.createTransaction(accountId, request);

        // PASO 4: Verificar resultados
        assertNotNull(actualResult);
        assertEquals(expectedResponse.id(), actualResult.id());
        assertEquals(expectedResponse.amount(), actualResult.amount());
        assertEquals(expectedResponse.type(), actualResult.type());

        // PASO 5: Verificar interacciones
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transactionEntity);
        verify(transactionMapper).toEntity(request);
        verify(transactionMapper).toResponse(transactionEntity);
    }

    // TEST 2: createTransaction - Caso malo (WITHDRAWAL con saldo insuficiente)
    @Test
    @DisplayName("createTransaction - Debería lanzar excepción si saldo insuficiente en retiro")
    void createTransaction_Withdrawal_InsufficientFunds() {
        // PASO 1: Datos de entrada
        Long accountId = 1L;
        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("600.00"),
                Transaction.TransactionType.WITHDRAWAL,
                accountId
        );

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("500.00"));

        // PASO 2: Configurar mocks
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // PASO 3: Ejecutar y capturar excepción
        InsufficientFundsException exception = assertThrows(
                InsufficientFundsException.class,
                () -> transactionService.createTransaction(accountId, request)
        );

        // PASO 4: Verificar mensaje
        assertTrue(exception.getMessage().contains("Saldo insuficiente"));

        // PASO 5: Verificar interacciones
        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    //  TEST 3: getTransactionById - Caso exitoso
    @Test
    @DisplayName("getTransactionById - Debería retornar transacción exitosamente")
    void getTransactionById_Success() {
        // PASO 1: Datos de entrada y salida esperados
        Long transactionId = 10L;

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setAmount(new BigDecimal("200.00"));
        transaction.setType(Transaction.TransactionType.DEPOSIT);

        TransactionResponseDTO expectedResponse = new TransactionResponseDTO(
                transactionId,
                new BigDecimal("200.00"),
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now(),
                1L
        );

        // PASO 2: Configurar mocks
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toResponse(transaction)).thenReturn(expectedResponse);

        // PASO 3: Ejecutar
        TransactionResponseDTO actualResult = transactionService.getTransactionById(transactionId);

        // PASO 4: Verificar resultados
        assertNotNull(actualResult);
        assertEquals(expectedResponse.id(), actualResult.id());
        assertEquals(expectedResponse.amount(), actualResult.amount());

        // PASO 5: Verificar interacciones
        verify(transactionRepository).findById(transactionId);
        verify(transactionMapper).toResponse(transaction);
    }

    //  TEST 4: getTransactionById - Caso malo
    @Test
    @DisplayName("getTransactionById - Debería lanzar excepción si transacción no existe")
    void getTransactionById_NotFound() {
        // PASO 1: Datos de entrada
        Long transactionId = 999L;

        // PASO 2: Configurar mocks
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // PASO 3: Ejecutar
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> transactionService.getTransactionById(transactionId)
        );

        // PASO 4: Verificar mensaje
        assertTrue(exception.getMessage().contains("Transacción no encontrada"));

        // PASO 5: Verificar interacciones
        verify(transactionRepository).findById(transactionId);
        verify(transactionMapper, never()).toResponse(any());
    }

    // TEST 5: getAllTransactions - Con transacciones
    @Test
    @DisplayName("getAllTransactions - Debería retornar lista con transacciones")
    void getAllTransactions_WithResults() {
        // PASO 1: Datos de entrada y salida
        Transaction t1 = new Transaction();
        t1.setId(1L);
        Transaction t2 = new Transaction();
        t2.setId(2L);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        TransactionResponseDTO r1 = new TransactionResponseDTO(
                1L, BigDecimal.TEN, Transaction.TransactionType.DEPOSIT, LocalDateTime.now(), 1L);
        TransactionResponseDTO r2 = new TransactionResponseDTO(
                2L, BigDecimal.ONE, Transaction.TransactionType.WITHDRAWAL, LocalDateTime.now(), 1L);

        // PASO 2: Configurar mocks
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transactionMapper.toResponse(t1)).thenReturn(r1);
        when(transactionMapper.toResponse(t2)).thenReturn(r2);

        // PASO 3: Ejecutar
        List<TransactionResponseDTO> actualResult = transactionService.getAllTransactions();

        // PASO 4: Verificar
        assertEquals(2, actualResult.size());
        assertEquals(1L, actualResult.get(0).id());
        assertEquals(2L, actualResult.get(1).id());

        // PASO 5: Verificar interacciones
        verify(transactionRepository).findAll();
        verify(transactionMapper, times(2)).toResponse(any(Transaction.class));
    }

    // TEST 6: getAllTransactions - Lista vacía
    @Test
    @DisplayName("getAllTransactions - Debería retornar lista vacía si no hay transacciones")
    void getAllTransactions_EmptyList() {
        // PASO 1: Datos de entrada
        List<Transaction> emptyList = Collections.emptyList();

        // PASO 2: Configurar mocks
        when(transactionRepository.findAll()).thenReturn(emptyList);

        // PASO 3: Ejecutar
        List<TransactionResponseDTO> actualResult = transactionService.getAllTransactions();

        // PASO 4: Verificar
        assertNotNull(actualResult);
        assertTrue(actualResult.isEmpty());

        // PASO 5: Verificar interacciones
        verify(transactionRepository).findAll();
        verify(transactionMapper, never()).toResponse(any());
    }

    // TEST 7: getTransactionsByAccountId - Con resultados
    @Test
    @DisplayName("getTransactionsByAccountId - Debería retornar lista de transacciones para cuenta")
    void getTransactionsByAccountId_WithResults() {
        // PASO 1: Datos de entrada y salida
        Long accountId = 5L;
        Transaction t1 = new Transaction();
        t1.setId(1L);
        Transaction t2 = new Transaction();
        t2.setId(2L);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        TransactionResponseDTO r1 = new TransactionResponseDTO(
                1L, BigDecimal.TEN, Transaction.TransactionType.DEPOSIT, LocalDateTime.now(), accountId);
        TransactionResponseDTO r2 = new TransactionResponseDTO(
                2L, BigDecimal.ONE, Transaction.TransactionType.WITHDRAWAL, LocalDateTime.now(), accountId);

        // PASO 2: Configurar mocks
        when(transactionRepository.findByAccountId(accountId)).thenReturn(transactions);
        when(transactionMapper.toResponse(t1)).thenReturn(r1);
        when(transactionMapper.toResponse(t2)).thenReturn(r2);

        // PASO 3: Ejecutar
        List<TransactionResponseDTO> actualResult = transactionService.getTransactionsByAccountId(accountId);

        // PASO 4: Verificar
        assertEquals(2, actualResult.size());
        assertEquals(accountId, actualResult.get(0).accountId());
        assertEquals(accountId, actualResult.get(1).accountId());

        // PASO 5: Verificar interacciones
        verify(transactionRepository).findByAccountId(accountId);
        verify(transactionMapper, times(2)).toResponse(any(Transaction.class));
    }

    // TEST 8: getTransactionsByAccountId - Lista vacía
    @DisplayName("getTransactionsByAccountId - Debería retornar lista vacía si no hay transacciones")
    void getTransactionsByAccountId_EmptyList() {
        // PASO 1: Datos de entrada
        Long accountId = 5L;

        // PASO 2: Configurar mocks
        when(transactionRepository.findByAccountId(accountId)).thenReturn(Collections.emptyList());

        // PASO 3: Ejecutar
        List<TransactionResponseDTO> actualResult = transactionService.getTransactionsByAccountId(accountId);

        // PASO 4: Verificar
        assertNotNull(actualResult);
        assertTrue(actualResult.isEmpty());

        // PASO 5: Verificar interacciones
        verify(transactionRepository).findByAccountId(accountId);
        verify(transactionMapper, never()).toResponse(any());
    }
}


