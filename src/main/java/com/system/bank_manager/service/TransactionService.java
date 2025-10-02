package com.system.bank_manager.service;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.request.TransferRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {

    void transferBetweenAccounts(TransferRequestDTO request);
    TransactionResponseDTO createTransaction(Long accountId, com.system.bank_manager.dto.request.TransactionRequestDTO request);
    List<TransactionResponseDTO> getAllTransactions();
    TransactionResponseDTO getTransactionById(Long id);
    List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId);
}