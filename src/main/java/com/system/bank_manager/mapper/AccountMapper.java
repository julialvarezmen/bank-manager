package com.system.bank_manager.mapper;

import com.system.bank_manager.dto.request.AccountRequestDTO;
import com.system.bank_manager.dto.response.AccountResponseDTO;
import com.system.bank_manager.entity.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "user", ignore = true)
    Account toEntity(AccountRequestDTO dto);

    @Mapping(target = "userId", source = "user.id")
    AccountResponseDTO toResponse(Account account);
}
