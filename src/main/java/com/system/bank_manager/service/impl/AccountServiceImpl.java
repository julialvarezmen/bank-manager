package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.AccountRequestDTO;
import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.Transaction;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.repository.TransactionRepository;
import com.system.bank_manager.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        Account account = new Account();
        // Mapea los campos de request a account
        // account.setCampo(request.getCampo());
        Account saved = accountRepository.save(account);
        return mapToResponseDTO(saved);
    }

    @Override
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        // Actualiza los campos de account con los datos de request
        Account updated = accountRepository.save(account);
        return mapToResponseDTO(updated);
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return mapToResponseDTO(account);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    // Metodo auxiliar para mapear Account a AccountResponseDTO
   private AccountResponseDTO mapToResponseDTO(Account account) {
       return new AccountResponseDTO(
           account.getId(),
           account.getAccountNumber(),
           account.getBalance(),
           account.getUser().getId()
       );
   }
}