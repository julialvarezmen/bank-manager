package com.system.bank_manager.controller;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.entity.Transaction;
import com.system.bank_manager.exception.InsufficientFundsException;
import com.system.bank_manager.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionController - Depósitos")
class TransactionControllerDepositTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    // ==================== TEST 1: Depósito exitoso ====================
    @Test
    @DisplayName("createTransaction - Debería realizar depósito exitosamente")
    void createTransaction_Deposit_Success() {
        // PASO 1: Crear request
        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("1000.00"),
                Transaction.TransactionType.DEPOSIT,
                1L
        );

        // PASO 2: Crear respuesta esperada
        TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                1L,
                new BigDecimal("1000.00"),
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now(),
                1L
        );

        // PASO 3: Configurar mock
        when(transactionService.createTransaction(1L, request)).thenReturn(responseDTO);

        // PASO 4: Ejecutar
        ResponseEntity<TransactionResponseDTO> response = transactionController.createTransaction(1L, request);

        // PASO 5: Verificar resultados
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());

        // PASO 6: Verificar interacciones
        verify(transactionService).createTransaction(1L, request);
    }

    // ==================== TEST 2: Depósito fallido (monto inválido) ====================
    @Test
    @DisplayName("createTransaction - Debería lanzar excepción si monto inválido")
    void createTransaction_Deposit_Fail() {
        // PASO 1: Crear request con monto negativo
        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("-100.00"),
                Transaction.TransactionType.DEPOSIT,
                1L
        );

        // PASO 2: Configurar mock para lanzar excepción
        doThrow(new IllegalArgumentException("El monto debe ser mayor a 0"))
                .when(transactionService).createTransaction(1L, request);

        // PASO 3: Ejecutar y capturar excepción
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transactionController.createTransaction(1L, request)
        );

        // PASO 4: Verificar mensaje
        assertTrue(exception.getMessage().contains("mayor a 0"));

        // PASO 5: Verificar interacciones
        verify(transactionService).createTransaction(1L, request);
    }
}

