package com.learn.ms.notification.presenter.rest.api;

import com.learn.ms.notification.common.utils.ResponseUtils;
import com.learn.ms.notification.core.domain.enums.ResponseMessage;
import com.learn.ms.notification.core.domain.model.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HomeResource extends BaseResource {

    @GetMapping("/")
    public ApiResponse<String> getApiResponse() {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), "Notification Service is running!");
    }

}
