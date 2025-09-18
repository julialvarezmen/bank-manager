package com.system.bank_manager.controller;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Registrar transacción (depósito o retiro)
   @PostMapping("/{accountId}")
   public ResponseEntity<TransactionResponseDTO> createTransaction(
       @PathVariable Long accountId,
       @RequestBody TransactionRequestDTO request
   ) {
       return ResponseEntity.ok(transactionService.createTransaction(accountId, request));
   }

    // Listar todas las transacciones
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // Obtener transacción por ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
