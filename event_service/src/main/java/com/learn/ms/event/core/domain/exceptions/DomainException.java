package com.learn.ms.event.core.domain.exceptions;


import com.learn.ms.event.core.domain.enums.ResponseMessage;

public class DomainException extends CustomRootException {
    private static final String MESSAGE_CODE = "EAC412";

    public DomainException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public DomainException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
