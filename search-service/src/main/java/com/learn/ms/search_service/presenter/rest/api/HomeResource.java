package com.learn.ms.search_service.presenter.rest.api;


import com.learn.ms.search_service.common.utils.ResponseUtils;
import com.learn.ms.search_service.core.domain.enums.ResponseMessage;
import com.learn.ms.search_service.core.domain.model.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HomeResource extends BaseResource {

    @GetMapping("/")
    public String test() {
        return "Search Service is running!";
    }


    @GetMapping("/get")
    public ApiResponse<String> getApiResponse() {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), "Search Service is running!");
    }

}
