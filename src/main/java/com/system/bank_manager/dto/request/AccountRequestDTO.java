package com.system.bank_manager.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AccountRequestDTO(

        @NotBlank(message = "El número de cuenta es obligatorio")
        @Pattern(regexp = "^[0-9]{6,12}$", message = "El número de cuenta debe tener entre 6 y 12 dígitos numéricos"
        )
        String accountNumber,

        @NotNull(message = "El saldo inicial no puede ser nulo")
        @DecimalMin(value = "0.00", inclusive = true, message = "El saldo inicial no puede ser negativo")
        BigDecimal balance,

        @NotNull(message = "El ID del usuario es obligatorio")
        @Min(value = 1, message = "El ID del usuario debe ser un número natural (mayor o igual a 1)")
        Long userId
) {}

