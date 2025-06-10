package com.banking.core_banking.infra.mapper.user;

import com.banking.core_banking.domain.model.dto.user.request.AddressCreateRequest;
import com.banking.core_banking.domain.model.utils.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressCreateRequest dto);
}
