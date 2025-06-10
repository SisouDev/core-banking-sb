package com.banking.core_banking.infra.mapper.invoice;

import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceItemResponse;
import com.banking.core_banking.domain.model.entities.invoice.InvoiceItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceItemMapper {
    InvoiceItemResponse toDto(InvoiceItem item);

    List<InvoiceItemResponse> toDtoList(List<InvoiceItem> items);
}
