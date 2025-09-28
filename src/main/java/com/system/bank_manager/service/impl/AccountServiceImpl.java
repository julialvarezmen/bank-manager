package com.system.bank_manager.service.impl;

import com.system.bank_manager.dto.request.AccountRequestDTO;
import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.entity.Account;
import com.system.bank_manager.entity.Transaction;
import com.system.bank_manager.entity.User;
import com.system.bank_manager.exception.DuplicateAccountException;
import com.system.bank_manager.repository.AccountRepository;
import com.system.bank_manager.repository.TransactionRepository;
import com.system.bank_manager.repository.UserRepository;
import com.system.bank_manager.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        if (accountRepository.existsByAccountNumber(request.accountNumber())) {
            throw new DuplicateAccountException("Ya existe una cuenta con este número: " + request.accountNumber());
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + request.userId()));


        Account account = new Account();
        account.setAccountNumber(request.accountNumber());
        account.setBalance(request.balance());
        account.setUser(user);

        Account saved = accountRepository.save(account);
        return mapToResponseDTO(saved);
    }

    @Override
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));


        account.setAccountNumber(request.accountNumber());
        account.setBalance(request.balance());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + request.userId()));
        account.setUser(user);

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
                .orElseThrow(() -> new EntityNotFoundException(("Cuenta no encontrada con ID: " + id)));
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