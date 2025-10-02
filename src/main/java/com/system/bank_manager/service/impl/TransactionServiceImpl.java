package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.request.TransferRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.Transaction;
import com.system.bank_manager.exception.InsufficientFundsException;
import com.system.bank_manager.mapper.TransactionMapper;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.repository.TransactionRepository;
import com.system.bank_manager.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + accountId));

        // Usamos directamente el enum
        if (request.type() == Transaction.TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(request.amount()));
        } else if (request.type() == Transaction.TransactionType.WITHDRAWAL) {
            if (account.getBalance().compareTo(request.amount()) < 0) {
                throw new InsufficientFundsException("Saldo insuficiente para realizar el retiro");
            }
            account.setBalance(account.getBalance().subtract(request.amount()));
        } else {
            throw new IllegalArgumentException("Tipo de transacción no válido: " + request.type());
        }

        accountRepository.save(account);

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setAccount(account);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public void transferBetweenAccounts(TransferRequestDTO request) {
        // Validación inicial
        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new IllegalArgumentException("No puedes transferir dinero a la misma cuenta");
        }

        // Buscar cuentas
        Account fromAccount = accountRepository.findById(request.fromAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta origen no encontrada con ID: " + request.fromAccountId()));
        Account toAccount = accountRepository.findById(request.toAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta destino no encontrada con ID: " + request.toAccountId()));

        // Verificar fondos
        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente en la cuenta origen");
        }

        // Actualizar saldos
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
        toAccount.setBalance(toAccount.getBalance().add(request.amount()));

        // Registrar transacciones
        LocalDateTime now = LocalDateTime.now();

        transactionRepository.save(createTransaction(request.amount(), Transaction.TransactionType.WITHDRAWAL, fromAccount, now));
        transactionRepository.save(createTransaction(request.amount(), Transaction.TransactionType.DEPOSIT, toAccount, now));
    }

    private Transaction createTransaction(BigDecimal amount, Transaction.TransactionType type, Account account, LocalDateTime date) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setAccount(account);
        transaction.setDate(date);
        return transaction;
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }
    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    @Override
    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transacción no encontrada con ID: " + id));
        return transactionMapper.toResponse(transaction);
    }
}