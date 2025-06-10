package com.banking.core_banking.exceptions.product;

import com.banking.core_banking.exceptions.others.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoanTermOutOfBoundsException extends BusinessException {
  public LoanTermOutOfBoundsException(String message) {
    super(message);
  }
}
