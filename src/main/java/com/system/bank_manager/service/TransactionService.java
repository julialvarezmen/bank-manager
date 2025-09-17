package com.system.bank_manager.service;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(Long accountId, TransactionRequestDTO request);
    List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId);
}