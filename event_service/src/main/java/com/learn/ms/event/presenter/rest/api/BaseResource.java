package com.learn.ms.event.presenter.rest.api;


import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.service.LocaleMessageService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseResource {

    protected LocaleMessageService localeMessageService;

    @Autowired
    public void setLocaleMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public String getMessage(ResponseMessage key) {
        return localeMessageService.getLocalMessage(key);
    }
}
