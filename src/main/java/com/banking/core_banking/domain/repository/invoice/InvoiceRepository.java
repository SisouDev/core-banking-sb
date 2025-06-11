package com.banking.core_banking.domain.repository.invoice;

import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByCreditFunctionCardIdOrderByReferenceMonthDesc(Long cardId, Pageable pageable);
}
