package com.learn.ms.notification.core.domain.enums;

import com.learn.ms.notification.core.domain.exceptions.NotificationDomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum NotificationTemplateName {

    WELCOME_EMAIL(1001, "welcome"),

    ;

    private final Integer notificationCode;
    private final String emailTemplateName;

    public static String getEmailTemplateName(int notificationCode) {
        NotificationTemplateName result = Arrays.stream(NotificationTemplateName.values())
                .filter(item -> item.getNotificationCode() == notificationCode)
                .findFirst()
                .orElse(null);

        if (result != null) {
            return result.getEmailTemplateName();
        } else {
            throw new NotificationDomainException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }
    }
}
