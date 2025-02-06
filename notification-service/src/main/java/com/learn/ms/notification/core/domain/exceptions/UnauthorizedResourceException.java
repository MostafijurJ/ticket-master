package com.learn.ms.notification.core.domain.exceptions;


import com.learn.ms.notification.core.domain.enums.ResponseMessage;

public class UnauthorizedResourceException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES401";

    public UnauthorizedResourceException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public UnauthorizedResourceException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
