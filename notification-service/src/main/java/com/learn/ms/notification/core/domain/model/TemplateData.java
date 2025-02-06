package com.learn.ms.notification.core.domain.model;

import com.learn.ms.notification.core.domain.enums.NotificationType;
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
public class TemplateData {
    private List<NotificationType> notificationTypes;
    private List<String> toEmailList;
    private List<String> toPhoneNumberList;
    private Integer notificationCode;
    private Map<String, Object> additionalFields;
}
