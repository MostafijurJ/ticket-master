package com.learn.ms.event.presenter.rest.api;

import com.learn.ms.event.common.utils.AppUtils;
import com.learn.ms.event.common.utils.ResponseUtils;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.model.ApiResponse;
import com.learn.ms.event.core.domain.request.PerformerRequest;
import com.learn.ms.event.core.domain.response.PerformerResponse;
import com.learn.ms.event.core.service.PerformerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/performers")
@Tag(name = "Performer", description = "Performer API CRUD operations")
@RequiredArgsConstructor
public class PerformerResource extends BaseResource {
    private final PerformerService performerService;

    @PostMapping("/create")
    @Operation(summary = "Create Performer", description = "Creates a new performer with the provided details.")
    public ApiResponse<PerformerResponse> createPerformer(@Valid @RequestBody PerformerRequest performer) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.EVENT_CREATED), performerService.createPerformer(performer));
    }

    @GetMapping
    @Operation(summary = "Get All Performers", description = "Retrieves a list of all performers.")
    public ApiResponse<Page<PerformerResponse>> getAllPerformers(@ParameterObject Pageable pageable) {
        Page<PerformerResponse> performers = performerService.getAllPerformers(pageable);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), performers);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get Performer by ID", description = "Retrieves the details of a performer by its ID.")
    public ApiResponse<PerformerResponse> getPerformerById(@PathVariable Long id) {
        PerformerResponse performerResponse = performerService.getPerformerById(id);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), performerResponse);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Performer", description = "Updates the details of an existing performer by its ID.")
    public ApiResponse<PerformerResponse> updatePerformer(@PathVariable Long id, @RequestBody PerformerRequest performer) {
        PerformerResponse performerResponse = performerService.updatePerformer(id, performer);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), performerResponse);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Performer", description = "Deletes a performer by its ID.")
    public ApiResponse<PerformerResponse> deletePerformer(@PathVariable Long id) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), performerService.deletePerformer(id));
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if Performer Exists", description = "Checks if a performer with the provided name exists.")
    public ApiResponse<Boolean> performerExists(@RequestParam String name) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), performerService.performerExists(name));
    }

}
