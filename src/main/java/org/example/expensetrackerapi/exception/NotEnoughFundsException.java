package org.example.projektidemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.InsufficientResourcesException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotEnoughFundsException extends InsufficientResourcesException {
    public NotEnoughFundsException(String message) {
        super(message);
    }
}
