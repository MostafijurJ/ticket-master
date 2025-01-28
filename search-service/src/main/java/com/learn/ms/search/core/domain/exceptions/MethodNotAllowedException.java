package com.learn.ms.search.core.domain.exceptions;


import com.learn.ms.search.core.domain.enums.ResponseMessage;

public class MethodNotAllowedException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES405";

    public MethodNotAllowedException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public MethodNotAllowedException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
