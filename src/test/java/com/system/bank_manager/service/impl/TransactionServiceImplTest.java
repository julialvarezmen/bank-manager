package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.Transaction;
import com.system.bank_manager.exception.InsufficientFundsException;
import com.system.bank_manager.mapper.TransactionMapper;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.repository.TransactionRepository;
import com.system.bank_manager.service.impl.TransactionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.system.bank_manager.entity.Transaction.TransactionType.RETIRO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        accountRepository = mock(AccountRepository.class);
        transactionMapper = mock(TransactionMapper.class);
        transactionService = new TransactionServiceImpl(transactionRepository, accountRepository, transactionMapper);
    }
    @Test
    void createTransaction_whenSufficientFunds_createsTransaction() {
        // 1. Datos de entrada
        Long accountId = 1L;
        TransactionRequestDTO request = new TransactionRequestDTO(
                BigDecimal.valueOf(200),
                Transaction.TransactionType.WITHDRAWAL,
                accountId
        );

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(500));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setAccount(account);

        TransactionResponseDTO expectedResponse = new TransactionResponseDTO(
                1L,
                BigDecimal.valueOf(200),
                Transaction.TransactionType.WITHDRAWAL,
                LocalDateTime.now(),
                accountId
        );

        // 2. Simulaciones
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(transactionMapper.toEntity(request)).thenReturn(transaction);
        Mockito.when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Mockito.when(transactionMapper.toResponse(transaction)).thenReturn(expectedResponse);

        // 3. Ejecución
        TransactionResponseDTO response = transactionService.createTransaction(accountId, request);

        // 4. Verificaciones
        assertNotNull(response);
        assertEquals(expectedResponse.id(), response.id());
        assertEquals(expectedResponse.amount(), response.amount());
        assertEquals(expectedResponse.type(), response.type());
        assertEquals(expectedResponse.accountId(), response.accountId());

        // 5. Interacciones
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(account);
        verify(transactionMapper).toEntity(request);
        verify(transactionRepository).save(transaction);
        verify(transactionMapper).toResponse(transaction);
    }
    @Test
    void createTransaction_whenInsufficientFunds_throwsException() {
        // 1. Datos de entrada
        Long accountId = 1L;
        TransactionRequestDTO request = new TransactionRequestDTO(
                BigDecimal.valueOf(600),
                Transaction.TransactionType.WITHDRAWAL,
                accountId
        );

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(500));

        // 2. Simulaciones
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // 3. Ejecución
        InsufficientFundsException exception = assertThrows(
                InsufficientFundsException.class,
                () -> transactionService.createTransaction(accountId, request)
        );

        // 4. Verificaciones
        assertEquals("Saldo insuficiente para realizar el retiro", exception.getMessage());

        // 5. Interacciones
        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any());
        verifyNoInteractions(transactionMapper, transactionRepository);
    }


}
