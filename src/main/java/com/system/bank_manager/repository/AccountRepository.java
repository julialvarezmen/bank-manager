package com.system.bank_manager.repository;

import com.system.bank_manager.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Buscar cuenta por número de cuenta
    Optional<Account> findByAccountNumber(String accountNumber);

    // Listar todas las cuentas de un usuario
    java.util.List<Account> findByUserId(Long userId);

        boolean existsByAccountNumber(@NotBlank(message = "El número de cuenta es obligatorio") @Pattern(regexp = "^[0-9]{6,12}$", message = "El número de cuenta debe tener entre 6 y 12 dígitos numéricos"
        ) String s);
}
