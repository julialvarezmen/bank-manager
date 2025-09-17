package com.system.bank_manager.service;

import com.system.bank_manager.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account createAccount(Account account);
    Optional<Account> getAccountById(Long id);
    Optional<Account> getAccountByNumber(String accountNumber);
    List<Account> getAccountsByUser(Long userId);
    BigDecimal getBalance(Long accountId);
    void deposit(Long accountId, BigDecimal amount);
    void withdraw(Long accountId, BigDecimal amount);
}

