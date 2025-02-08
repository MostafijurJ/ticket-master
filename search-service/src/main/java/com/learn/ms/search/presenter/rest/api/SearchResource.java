package com.learn.ms.search.presenter.rest.api;


import com.learn.ms.search.common.utils.ResponseUtils;
import com.learn.ms.search.core.domain.enums.ResponseMessage;
import com.learn.ms.search.core.domain.model.ApiResponse;
import com.learn.ms.search.core.domain.response.EventElasticResponse;
import com.learn.ms.search.core.domain.utils.AppUtils;
import com.learn.ms.search.core.service.IEventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Search", description = "APIs for Search Management")
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL)
public class SearchResource extends BaseResource {
    private final IEventService eventSearchService;

    @GetMapping("/events")
    public ApiResponse<List<EventElasticResponse>> searchEvents(@RequestParam("q") String query) {
        final var results = eventSearchService.searchEvents(query);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), results);
    }
}
