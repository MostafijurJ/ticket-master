package com.learn.ms.notification.core.domain.exceptions;


import com.learn.ms.notification.core.domain.enums.ResponseMessage;

public class NotificationDomainException extends CustomRootException {
    private static final String MESSAGE_CODE = "ENT412";

    public NotificationDomainException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public NotificationDomainException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
