package com.learn.ms.event.data.entity;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "booking_history")
public class BookingHistory extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "pnr_number")
    private String pnrNumber;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "seat_number")
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketStatus status;

    @Column(name = "booking_time")
    private Date bookingTime;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_source")
    private String paymentSource;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "account_number")
    private String accountNumber;

}
