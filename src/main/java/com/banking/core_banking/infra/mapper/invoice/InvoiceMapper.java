package com.banking.core_banking.infra.mapper.invoice;

import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceDetailsResponse;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceSummaryResponse;
import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {InvoiceItemMapper.class})
public interface InvoiceMapper {

    InvoiceSummaryResponse toSummaryDto(Invoice invoice);

    @Mapping(source = "items", target = "items")
    @Mapping(target = "paidAmount", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "dueAmount", expression = "java(invoice.getTotalAmount().subtract(java.math.BigDecimal.ZERO))")
    InvoiceDetailsResponse toDetailsDto(Invoice invoice);
}
