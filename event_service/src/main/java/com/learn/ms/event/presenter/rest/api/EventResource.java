package com.learn.ms.event.presenter.rest.api;

import com.learn.ms.event.common.utils.AppUtils;
import com.learn.ms.event.common.utils.ResponseUtils;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.model.ApiResponse;
import com.learn.ms.event.core.domain.request.EventRequest;
import com.learn.ms.event.core.domain.response.EventResponse;
import com.learn.ms.event.core.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@Tag(name = "Event", description = "APIs for Event Management")
@RequiredArgsConstructor
public class EventResource extends BaseResource {
    private final EventService eventService;

    @PostMapping("/create")
    @Operation(summary = "Create Event", description = "Creates a new event with the provided details.")
    public ApiResponse<EventResponse> createEvent(@RequestBody @Valid EventRequest eventRequest) {
        EventResponse eventResponse = eventService.createEvent(eventRequest);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.EVENT_CREATED), eventResponse);
    }

    @GetMapping
    @Operation(summary = "Get All Events", description = "Retrieves a paginated list of all events.")
    public ApiResponse<Page<EventResponse>> getAllEvents(@ParameterObject Pageable pageable) {
        Page<EventResponse> events = eventService.getAllEvents(pageable);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), events);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get Event by ID", description = "Retrieves the details of an event by its ID.")
    public ApiResponse<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse eventResponse = eventService.getEventById(id);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), eventResponse);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Event", description = "Updates the details of an existing event by its ID.")
    public ApiResponse<EventResponse> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventRequest) {
        EventResponse eventResponse = eventService.updateEvent(id, eventRequest);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), eventResponse);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Event", description = "Deletes an event by its ID.")
    public ApiResponse<EventResponse> deleteEvent(@PathVariable Long id) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), eventService.deleteEvent(id));
    }

}
