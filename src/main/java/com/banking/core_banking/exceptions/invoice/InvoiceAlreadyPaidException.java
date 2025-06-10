package com.banking.core_banking.exceptions.invoice;

import com.banking.core_banking.exceptions.others.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvoiceAlreadyPaidException extends BusinessException {
  public InvoiceAlreadyPaidException(String message) {
    super(message);
  }
}