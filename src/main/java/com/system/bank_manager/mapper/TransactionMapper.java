package com.system.bank_manager.mapper;

import com.system.bank_manager.dto.request.TransactionRequestDTO;
import com.system.bank_manager.dto.response.TransactionResponseDTO;
import com.system.bank_manager.entity.Transaction;
import org.mapstruct.*;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDateTime.now())")
    @Mapping(target = "type", expression = "java(Transaction.TransactionType.valueOf(dto.type()))")
    @Mapping(target = "account", ignore = true)
    Transaction toEntity(TransactionRequestDTO dto);

    @Mapping(target = "accountId", source = "account.id")
    TransactionResponseDTO toResponse(Transaction transaction);
}

