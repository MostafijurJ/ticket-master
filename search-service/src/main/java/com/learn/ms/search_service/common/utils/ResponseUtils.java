package com.learn.ms.search_service.common.utils;

import com.learn.ms.search_service.core.domain.enums.ApiResponseCode;
import com.learn.ms.search_service.core.domain.model.ApiResponse;

public class ResponseUtils {

    public static <T> ApiResponse<T> createSuccessResponseObject(String message) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
        apiResponse.setResponseMessage(message);
        return apiResponse;
    }

    public static <T> ApiResponse<T> createSuccessResponseObject(String message, T data) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
        apiResponse.setResponseMessage(message);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> ApiResponse<T> createApiResponse(String responseCode, String responseMessage, T data) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(responseCode);
        apiResponse.setResponseMessage(responseMessage);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> ApiResponse<T> createApiResponse(String responseCode, String responseMessage) {
        ApiResponse<T> apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(responseCode);
        apiResponse.setResponseMessage(responseMessage);
        return apiResponse;
    }
}
