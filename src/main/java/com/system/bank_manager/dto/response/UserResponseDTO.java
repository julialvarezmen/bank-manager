package com.system.bank_manager.dto.response;

public record UserResponseDTO(
        Long id,
        String dni,
        String name,
        String email
) {}
