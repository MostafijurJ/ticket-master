package com.learn.ms.search.core.domain.exceptions;


import com.learn.ms.search.core.domain.enums.ResponseMessage;

public class InvalidRequestDataException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES400";

    public InvalidRequestDataException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public InvalidRequestDataException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
