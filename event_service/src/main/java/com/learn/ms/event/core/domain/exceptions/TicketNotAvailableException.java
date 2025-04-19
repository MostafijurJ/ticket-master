package com.learn.ms.event.core.domain.exceptions;

import com.learn.ms.event.core.domain.enums.ResponseMessage;

public class TicketNotAvailableException extends CustomRootException {
    private static final String MESSAGE_CODE = "EES409";

    public TicketNotAvailableException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
    public TicketNotAvailableException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }
}
