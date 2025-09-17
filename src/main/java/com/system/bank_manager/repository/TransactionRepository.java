package com.system.bank_manager.repository;

import com.system.bank_manager.entity.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Buscar transacciones por cuenta
    List<Transaction> findByAccountId(Long accountId);

    // Buscar todas las transacciones de un tipo específico (ejemplo: todos los depósitos)
    List<Transaction> findByType(Transaction.TransactionType type);
}
