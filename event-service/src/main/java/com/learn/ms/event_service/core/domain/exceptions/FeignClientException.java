package com.learn.ms.event_service.core.domain.exceptions;


import com.learn.ms.event_service.core.domain.enums.ResponseMessage;

public class FeignClientException extends CustomRootException {
    private static final String MESSAGE_CODE = "ERPS400";

    public FeignClientException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public FeignClientException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
