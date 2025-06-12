package com.banking.core_banking.infra.mapper.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanProductCreateRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanProductUpdateRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanProductResponse;
import com.banking.core_banking.domain.model.entities.product.BankingProduct;
import com.banking.core_banking.domain.model.entities.product.LoanProduct;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    public BankingProductResponse toDto(BankingProduct product) {
        if (product instanceof LoanProduct loanProduct) {
            return toLoanProductDto(loanProduct);
        }
        throw new IllegalArgumentException("Unknown product type");
    }

    @Mapping(source = "active", target = "isActive")
    protected abstract LoanProductResponse toLoanProductDto(LoanProduct loanProduct);

    public LoanProduct toEntity(LoanProductCreateRequest request) {
        if (request == null) {
            return null;
        }
        return LoanProduct.create(
                request.name(),
                request.description(),
                request.minAmount(),
                request.maxAmount(),
                request.defaultInterestRate(),
                request.maxInstallments()
        );
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(LoanProductUpdateRequest request, @MappingTarget LoanProduct entity);
}
