package com.system.bank_manager.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a 0")
        BigDecimal amount,

        @NotBlank(message = "El tipo de transacción es obligatorio")
        @Pattern(
                regexp = "DEPOSITO|RETIRO",
                message = "El tipo de transacción debe ser DEPOSITO o RETIRO"
        )
        String type,

        @NotNull(message = "La cuenta es obligatoria")
        @Positive(message = "El ID de la cuenta debe ser un número positivo")
        Long accountId
) {}

