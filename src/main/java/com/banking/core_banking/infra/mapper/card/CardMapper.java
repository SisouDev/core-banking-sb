package com.banking.core_banking.infra.mapper.card;

import com.banking.core_banking.domain.model.dto.card.request.CreditLimitUpdateRequest;
import com.banking.core_banking.domain.model.dto.card.response.CardResponse;
import com.banking.core_banking.domain.model.dto.card.response.CardSummaryResponse;
import com.banking.core_banking.domain.model.dto.card.response.CreditFunctionResponse;
import com.banking.core_banking.domain.model.dto.card.response.DebitFunctionResponse;
import com.banking.core_banking.domain.model.entities.card.Card;
import com.banking.core_banking.domain.model.entities.card.CreditFunction;
import com.banking.core_banking.domain.model.entities.card.DebitFunction;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(source = "number", target = "maskedNumber", qualifiedByName = "maskCardNumber")
    CardResponse toDto(Card card);

    CreditFunctionResponse toDto(CreditFunction creditFunction);

    DebitFunctionResponse toDto(DebitFunction debitFunction);

    @Mapping(source = "number", target = "maskedNumber", qualifiedByName = "maskCardNumber")
    @Mapping(source = "debit", target = "isDebit")
    @Mapping(source = "credit", target = "isCredit")
    CardSummaryResponse toSummaryDto(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreditLimitUpdateRequest dto, @MappingTarget CreditFunction entity);

    @Named("maskCardNumber")
    default String maskCardNumber(String number) {
        if (number == null || number.length() <= 4) {
            return "****";
        }
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}
