package com.learn.ms.event.core.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PerformerRequest {

    @NotBlank(message = "Invalid Name: Empty/Null name")
    private String name;
}
