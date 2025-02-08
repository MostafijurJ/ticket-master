package com.learn.ms.event.core.domain.event;

import com.learn.ms.event.core.domain.enums.NotificationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@Accessors(chain = true)
public class NotificationTemplateEvent {
    private List<NotificationType> notificationTypes;
    private List<String> toEmailList;
    private List<String> toPhoneNumberList;
    private String featureCode;
    private Map<String, Object> additionalFields;
}
