package com.system.bank_manager.dto.response;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Long id,
        String accountNumber,
        BigDecimal balance,
        Long userId
) {}
