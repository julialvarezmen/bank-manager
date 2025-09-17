package com.system.bank_manager.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        com.system.bank_manager.entity.Transaction.TransactionType type,
        LocalDateTime date,
        Long accountId
) {}
