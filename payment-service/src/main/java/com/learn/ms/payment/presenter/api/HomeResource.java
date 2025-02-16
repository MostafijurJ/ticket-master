package com.learn.ms.payment.presenter.api;
import com.learn.ms.notification.core.domain.model.ApiResponse;
import com.learn.ms.payment.common.utils.ResponseUtils;
import com.learn.ms.payment.core.domain.enums.ResponseMessage;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HomeResource extends BaseResource {

    @GetMapping("/")
    public ApiResponse<String> getApiResponse() {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), "Payment Service is Running.....!");
    }

}
