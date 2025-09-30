package com.system.bank_manager.dto.request;

import jakarta.validation.constraints.*;

public record CreateUserDTO(

        @NotBlank(message = "El DNI es obligatorio")
        String dni,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String name,

        @Email(message = "Debe ser un email válido")
        String email,

        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres y al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=!")
        @NotNull(message = "La contraseña no puede ser nula")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
                message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=!)"
        )
        String password
) {}
