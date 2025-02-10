package org.example.projektidemo.exception;

import java.util.zip.DataFormatException;

public class DateException extends DataFormatException {
    public DateException(String message) {
        super(message);
    }
}
