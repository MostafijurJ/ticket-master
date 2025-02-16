package com.learn.ms.payment.core.domain.exceptions;


import com.learn.ms.payment.core.domain.enums.ResponseMessage;

public class OperationFailedException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES101";

    public OperationFailedException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public OperationFailedException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
