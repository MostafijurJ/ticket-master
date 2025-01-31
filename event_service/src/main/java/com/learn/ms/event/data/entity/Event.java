package com.learn.ms.event.data.entity;

import com.learn.ms.event.core.domain.enums.EventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@Table(name = "EVENT")
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS", length = 50)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EVENT_DATE")
    private Date eventDate;

    @Column(name = "VENUE", columnDefinition = "TEXT")
    private String venue;

    @Column(name = "PERFORMERS", columnDefinition = "TEXT")
    private String performers;

}
