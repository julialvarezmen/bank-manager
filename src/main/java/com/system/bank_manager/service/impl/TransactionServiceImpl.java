package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.Transaction;
import com.system.bank_manager.mapper.TransactionMapper;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.repository.TransactionRepository;
import com.system.bank_manager.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(Long accountId, TransactionRequestDTO request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setAccount(account);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }
}