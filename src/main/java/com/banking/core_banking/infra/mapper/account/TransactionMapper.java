package com.banking.core_banking.infra.mapper.account;

import com.banking.core_banking.domain.model.dto.account.response.TransactionResponse;
import com.banking.core_banking.domain.model.entities.account.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "type", target = "transactionType")
    TransactionResponse toDto(Transaction transaction);

    List<TransactionResponse> toDtoList(List<Transaction> transactions);
}
