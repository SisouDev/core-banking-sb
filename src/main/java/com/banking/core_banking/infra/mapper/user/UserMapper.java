package com.banking.core_banking.infra.mapper.user;

import com.banking.core_banking.domain.model.dto.user.response.UserResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "role", target = "role")
    UserResponse toDto(User user);
}
