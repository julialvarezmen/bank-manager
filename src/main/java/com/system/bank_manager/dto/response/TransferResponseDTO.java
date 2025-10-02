package com.system.bank_manager.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponseDTO(
        BigDecimal amount,
        Long fromAccountId,
        Long toAccountId,
        LocalDateTime date
) {}