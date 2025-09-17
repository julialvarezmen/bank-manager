package com.system.bank_manager.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransactionRequestDTO(
        @NotNull(message = "El monto es obligatorio")
        BigDecimal amount,

        @NotNull(message = "El tipo de transacción es obligatorio")
        String type, // "DEPOSIT" o "WITHDRAWAL"

        @NotNull(message = "La cuenta es obligatoria")
        Long accountId
) {}
