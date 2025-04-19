package com.learn.ms.notification.core.domain.enums;

import com.learn.ms.notification.core.domain.exceptions.NotificationDomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum NotificationTemplateName {

    WELCOME_EMAIL("1001", "welcome"),
    EVENT_CREATE("1101", "event-create"),
    BOOK_TICKET("1110", "book-ticket"),

    ;

    private final String featureCode;
    private final String emailTemplateName;

    public static String getEmailTemplateName(String featureCode) {
        NotificationTemplateName result = Arrays.stream(NotificationTemplateName.values())
                .filter(item -> item.getFeatureCode().equalsIgnoreCase(featureCode))
                .findFirst()
                .orElse(null);

        if (result != null) {
            return result.getEmailTemplateName();
        } else {
            throw new NotificationDomainException(ResponseMessage.INTERNAL_SERVICE_EXCEPTION);
        }
    }
}
