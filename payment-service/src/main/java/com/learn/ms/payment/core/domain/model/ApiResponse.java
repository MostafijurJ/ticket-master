package com.learn.ms.notification.core.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.learn.ms.payment.common.utils.SensitiveCardNumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private String responseCode;

    @JsonSerialize(using = SensitiveCardNumSerializer.class)
    private String responseMessage;
    private T data;
}
