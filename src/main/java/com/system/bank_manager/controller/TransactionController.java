package com.system.bank_manager.controller;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.request.TransferRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transacciones", description = "Operaciones relacionadas con Transacciones")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Registrar transacción (depósito o retiro)
   @PostMapping("/accounts/{accountId}")
   @Operation(summary = "Registrar una transacción (depósito o retiro)")
   public ResponseEntity<TransactionResponseDTO> createTransaction(
       @PathVariable Long accountId,
       @Valid @RequestBody TransactionRequestDTO request
   ) {
       return ResponseEntity.ok(transactionService.createTransaction(accountId, request));
   }
    @PostMapping("/transfer")
    @Operation(summary = "Transferir dinero entre cuentas")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDTO request) {
        transactionService.transferBetweenAccounts(request);
        return ResponseEntity.ok("Transferencia realizada con éxito");
    }

    // Listar todas las transacciones
    @GetMapping
    @Operation(summary = "Lista todas las transacciones")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // Obtener transacción por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener transacción por ID")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
