package com.learn.ms.event.presenter.rest.api;

import com.learn.ms.event.common.utils.AppUtils;
import com.learn.ms.event.common.utils.ResponseUtils;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.model.ApiResponse;
import com.learn.ms.event.core.domain.request.BookingRequest;
import com.learn.ms.event.core.domain.response.BookingResponse;
import com.learn.ms.event.core.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL + "/booking")
@Tag(name = "Booking", description = "Booking API operations")
public class BookingResource extends BaseResource {
    private final BookingService bookingService;


    @PostMapping("/ticket")
    public ApiResponse<BookingResponse> getApiResponse(@RequestBody @Valid BookingRequest bookingRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), bookingService.bookTicket(bookingRequest));
    }

}
