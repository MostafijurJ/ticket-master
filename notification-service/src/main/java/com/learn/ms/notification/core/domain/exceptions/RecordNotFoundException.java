package com.learn.ms.notification.core.domain.exceptions;


import com.learn.ms.notification.core.domain.enums.ResponseMessage;

public class RecordNotFoundException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES404";

    public RecordNotFoundException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public RecordNotFoundException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
