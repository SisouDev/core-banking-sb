package com.banking.core_banking.infra.mapper.product;

import com.banking.core_banking.domain.model.dto.product.response.LoanDetailsResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanSummaryResponse;
import com.banking.core_banking.domain.model.entities.product.Loan;
import com.banking.core_banking.infra.mapper.user.CustomerMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        LoanInstallmentMapper.class,
        CustomerMapper.class
})
public interface LoanMapper {
    @Mapping(source = "loanProduct.name", target = "productName")
    LoanSummaryResponse toSummaryDto(Loan loan);

    @Mapping(source = "loanProduct.name", target = "productName")
    @Mapping(source = "loanInstallments", target = "installments")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "toSummaryDto")
    LoanDetailsResponse toDetailsDto(Loan loan);
}
