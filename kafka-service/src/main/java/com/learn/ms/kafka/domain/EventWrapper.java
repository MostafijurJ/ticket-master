package com.learn.ms.kafka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventWrapper<T> implements Serializable {
    private String eventId;
    private String correlationId;
    private Date eventDate;
    private T data;
}
