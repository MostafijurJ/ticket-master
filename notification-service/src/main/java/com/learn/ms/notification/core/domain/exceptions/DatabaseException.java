package com.learn.ms.notification.core.domain.exceptions;


import com.learn.ms.notification.core.domain.enums.ResponseMessage;

public class DatabaseException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES422";

    public DatabaseException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public DatabaseException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
