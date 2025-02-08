package com.learn.ms.notification.core.domain.enums;

import com.learn.ms.notification.core.domain.model.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {

    OPERATION_SUCCESSFUL("200"),

    INVALID_REQUEST_DATA("ERR400"),

    UNAUTHORIZED_RESOURCE_ACCESS("ERR401"),
    ACCESS_DENY_ERROR("ERR403"),
    RECORD_NOT_FOUND("ERR404"),
    METHOD_NOT_ALLOWED("ERR405"),
    DB_OPERATION_FAILED("ERR422"),
    SERVICE_DOMAIN_ERROR("ERR412"),

    UNHANDLED_EXCEPTION("ERR500"),
    INTER_SERVICE_COMMUNICATION_ERROR("ERR503"),
    ;

    private final String responseCode;

    public static boolean isOperationSuccessful(ApiResponse apiResponse) {
        return Objects.nonNull(apiResponse) && apiResponse.getResponseCode().equals(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
    }

    public static boolean isNotOperationSuccessful(ApiResponse apiResponse) {
        return !isOperationSuccessful(apiResponse);
    }

}
