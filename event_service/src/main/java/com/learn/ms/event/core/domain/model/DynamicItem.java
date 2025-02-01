package com.learn.ms.event.core.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DynamicItem implements Serializable {
    private String key;
    private String value;
}
