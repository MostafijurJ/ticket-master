package com.learn.ms.payment.presenter.api;

import com.learn.ms.payment.core.domain.enums.ResponseMessage;
import com.learn.ms.payment.core.service.LocaleMessageService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseResource {
    protected LocaleMessageService localeMessageService;

    @Autowired
    public void setLocaleMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    protected String getMessage(ResponseMessage key) {
        return localeMessageService.getLocalMessage(key);
    }
}
