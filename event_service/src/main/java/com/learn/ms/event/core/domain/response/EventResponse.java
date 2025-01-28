package com.learn.ms.event.core.domain.response;

import com.learn.ms.event.core.domain.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String date;
    private String location;
    private EventStatus status;
}
