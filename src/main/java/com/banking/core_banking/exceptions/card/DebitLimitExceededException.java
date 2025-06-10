package com.banking.core_banking.exceptions.card;

import com.banking.core_banking.exceptions.others.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DebitLimitExceededException extends BusinessException {
  public DebitLimitExceededException(String message) {
    super(message);
  }
}
