package com.learn.ms.search.core.domain.exceptions;


import com.learn.ms.search.core.domain.enums.ResponseMessage;

public class DomainException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES412";

    public DomainException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public DomainException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
