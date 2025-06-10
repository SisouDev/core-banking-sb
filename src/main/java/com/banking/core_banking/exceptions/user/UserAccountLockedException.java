package com.banking.core_banking.exceptions.user;

import com.banking.core_banking.exceptions.others.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAccountLockedException extends BusinessException {
  public UserAccountLockedException(String message) {
    super(message);
  }
}
