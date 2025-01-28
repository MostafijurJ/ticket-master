package com.learn.ms.event.data.entity;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TICKET")
public class Ticket extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "SEAT_NUMBER")
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private TicketCategory category;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "BOOKED")
    private Boolean booked;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}
