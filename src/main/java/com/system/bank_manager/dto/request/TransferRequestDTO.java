package com.system.bank_manager.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferRequestDTO(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a 0")
        BigDecimal amount,

        @NotNull(message = "La cuenta origen es obligatoria")
        @Positive(message = "El ID de la cuenta origen debe ser positivo")
        Long fromAccountId,

        @NotNull(message = "La cuenta destino es obligatoria")
        @Positive(message = "El ID de la cuenta destino debe ser positivo")
        Long toAccountId

) {}
