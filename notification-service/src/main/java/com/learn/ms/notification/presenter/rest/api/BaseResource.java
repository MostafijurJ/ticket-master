package com.learn.ms.notification.presenter.rest.api;

import com.learn.ms.notification.core.domain.enums.ResponseMessage;
import com.learn.ms.notification.core.service.LocaleMessageService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseResource {

    protected LocaleMessageService localeMessageService;

    @Autowired
    protected void setLocaleMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    protected String getMessage(ResponseMessage key) {
        return localeMessageService.getLocalMessage(key);
    }
}
