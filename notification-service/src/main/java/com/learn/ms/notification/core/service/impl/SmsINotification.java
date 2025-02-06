package com.learn.ms.notification.core.service.impl;

import com.learn.ms.notification.core.domain.model.TemplateData;
import com.learn.ms.notification.core.service.BaseService;
import com.learn.ms.notification.core.service.INotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsINotification extends BaseService implements INotificationStrategy {
    @Override
    public void sendNotification(TemplateData templateData) {
        logger.trace("Sending SMS to :  {}", templateData.getToPhoneNumberList());
    }
}
