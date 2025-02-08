package com.learn.ms.event.presenter.rest.api;

import com.learn.ms.event.common.utils.AppUtils;
import com.learn.ms.event.common.utils.ResponseUtils;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.model.ApiResponse;
import com.learn.ms.event.core.domain.response.TicketResponse;
import com.learn.ms.event.core.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/ticket")
@Tag(name = "Ticket", description = "APIs for Ticket Management")
@RequiredArgsConstructor
public class TicketResource extends BaseResource {
    private final TicketService ticketService;

    @GetMapping("/available/{eventId}")
    @Operation(summary = "Get Available Tickets for a Event", description = "Retrieves the available tickets for an event by its ID.")
    public ApiResponse<List<TicketResponse>> getAvailableTickets(@PathVariable Long eventId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), ticketService.getAvailableTickets(eventId));
    }


    @GetMapping("/available/count/{eventId}")
    @Operation(summary = "Get Available Tickets Count for a Event", description = "Retrieves the available tickets count for an event by its ID.")
    public ApiResponse<Map<String, Object>> getAvailableTicketsCount(@PathVariable Long eventId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), ticketService.getAvailableTicketCount(eventId));
    }


}
