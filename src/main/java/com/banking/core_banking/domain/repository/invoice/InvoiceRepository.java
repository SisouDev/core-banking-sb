package com.banking.core_banking.domain.repository.invoice;

import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import com.banking.core_banking.domain.model.enums.card.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByCreditFunctionCardIdOrderByReferenceMonthDesc(Long cardId, Pageable pageable);
    Optional<Invoice> findByCreditFunctionIdAndStatus(Long creditFunctionId, InvoiceStatus status);
    @Query("SELECT i FROM Invoice i WHERE i.creditFunction.card.account.customer.id = :customerId ORDER BY i.referenceMonth DESC")
    Page<Invoice> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}
