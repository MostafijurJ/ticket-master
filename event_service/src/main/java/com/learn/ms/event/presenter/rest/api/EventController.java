package com.learn.ms.event.presenter.rest.api;

import com.learn.ms.event.common.utils.ResponseUtils;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.model.ApiResponse;
import com.learn.ms.event.core.domain.request.EventRequest;
import com.learn.ms.event.core.domain.response.EventResponse;
import com.learn.ms.event.presenter.service.EventService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController extends BaseResource {
    private final EventService eventService;

    @PostMapping
    public ApiResponse<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        EventResponse eventResponse = eventService.createEvent(eventRequest);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.EVENT_CREATED), eventResponse);
    }

    @GetMapping
    public ApiResponse<Page<EventResponse>> getAllEvents(Pageable pageable) {
        Page<EventResponse> events = eventService.getAllEvents(pageable);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), events);
    }

    @GetMapping("/{id}")
    public ApiResponse<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse eventResponse = eventService.getEventById(id);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), eventResponse);
    }

    @PutMapping("/{id}")
    public ApiResponse<EventResponse> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventRequest) {
        EventResponse eventResponse = eventService.updateEvent(id, eventRequest);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), eventResponse);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<EventResponse> deleteEvent(@PathVariable Long id) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), eventService.deleteEvent(id));
    }


}
