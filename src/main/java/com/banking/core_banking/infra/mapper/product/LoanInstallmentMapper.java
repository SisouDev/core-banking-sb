package com.banking.core_banking.infra.mapper.product;

import com.banking.core_banking.domain.model.dto.product.response.LoanInstallmentResponse;
import com.banking.core_banking.domain.model.entities.product.LoanInstallment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanInstallmentMapper {
    LoanInstallmentResponse toDto(LoanInstallment installment);

    List<LoanInstallmentResponse> toDtoList(List<LoanInstallment> installments);
}
