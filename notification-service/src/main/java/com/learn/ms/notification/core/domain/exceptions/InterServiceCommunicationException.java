package com.learn.ms.notification.core.domain.exceptions;


import com.learn.ms.notification.core.domain.enums.ResponseMessage;

public class InterServiceCommunicationException extends CustomRootException {
    private static final String MESSAGE_CODE = "ES503";

    public InterServiceCommunicationException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public InterServiceCommunicationException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
