package com.system.bank_manager.service;

import com.system.bank_manager.dto.request.AccountRequestDTO;
import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// src/main/java/com/system/bank_manager/service/AccountService.java
public interface AccountService {
    AccountResponseDTO createAccount(AccountRequestDTO request);
    AccountResponseDTO updateAccount(Long id, AccountRequestDTO request);
    List<AccountResponseDTO> getAllAccounts();
    AccountResponseDTO getAccountById(Long id);
    void deleteAccount(Long id);

}

