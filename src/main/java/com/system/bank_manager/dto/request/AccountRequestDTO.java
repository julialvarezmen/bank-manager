package com.system.bank_manager.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AccountRequestDTO(
        @NotNull(message = "El número de cuenta es obligatorio")
        String accountNumber,

        @NotNull(message = "El saldo inicial no puede ser nulo")
        BigDecimal balance,

        @NotNull(message = "El ID del usuario es obligatorio")
        Long userId
) {}

