
package com.system.bank_manager.mapper;

import com.system.bank_manager.dto.request.CreateUserDTO;
import com.system.bank_manager.dto.response.UserResponseDTO;
import com.system.bank_manager.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(CreateUserDTO dto);
    UserResponseDTO toResponse(User user);
}