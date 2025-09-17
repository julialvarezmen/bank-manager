package com.system.bank_manager.dto.request;

public record UpdateUserDTO(
    String name,
    String email,
    String password
) {}