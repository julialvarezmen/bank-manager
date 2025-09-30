package com.system.bank_manager.dto.request;

import com.system.bank_manager.entity.Transaction;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a 0")
        BigDecimal amount,

        @NotNull(message = "El tipo de transacción es obligatorio")
        Transaction.TransactionType type,

        @NotNull(message = "La cuenta es obligatoria")
        @Positive(message = "El ID de la cuenta debe ser un número positivo")
        Long accountId
) {}

