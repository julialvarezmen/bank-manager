package com.system.bank_manager.dto.response;

import com.system.bank_manager.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        Transaction.TransactionType type,
        LocalDateTime date,
        Long accountId
) {}
