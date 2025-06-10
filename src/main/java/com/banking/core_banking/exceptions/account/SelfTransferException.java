package com.banking.core_banking.exceptions.account;

import com.banking.core_banking.exceptions.others.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SelfTransferException extends BusinessException {
    public SelfTransferException(String message) {
        super(message);
    }
}
