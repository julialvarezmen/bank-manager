package com.system.bank_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.bank_manager.dto.request.AccountRequestDTO;
import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.exception.DuplicateAccountException;
import com.system.bank_manager.exception.GlobalExceptionHandler;
import com.system.bank_manager.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    //  CASO EXITOSO
    @Test
    void createAccount_whenValidRequest_returnsCreatedAccount() throws Exception {
        // 1
        AccountRequestDTO request = new AccountRequestDTO("1234567890", BigDecimal.valueOf(5000), 1L);
        AccountResponseDTO response = new AccountResponseDTO(1L, "1234567890", BigDecimal.valueOf(5000), 1L);
        //2
        when(accountService.createAccount(any(AccountRequestDTO.class)))
                .thenReturn(response);

        // 3 y 4
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.balance").value(5000))
                .andExpect(jsonPath("$.userId").value(1L));

        // 5
        verify(accountService, times(1)).createAccount(any(AccountRequestDTO.class));
    }

    // CASO DE ERROR
    @Test
    void createAccount_whenDuplicateAccount_returnsConflict() throws Exception {
        // 1
        AccountRequestDTO request = new AccountRequestDTO("1234567890", BigDecimal.valueOf(5000), 1L);
        // 2
        when(accountService.createAccount(any(AccountRequestDTO.class)))
                .thenThrow(new DuplicateAccountException("Ya existe una cuenta con este número: 1234567890"));

        // 3 y 4
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE_ACCOUNT"))
                .andExpect(jsonPath("$.message").value("Ya existe una cuenta con este número: 1234567890"))
                .andExpect(jsonPath("$.timestamp").exists());

        // 5
        verify(accountService, times(1)).createAccount(any(AccountRequestDTO.class));
    }
}

