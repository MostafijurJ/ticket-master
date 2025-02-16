package com.learn.ms.event.core.service.impl;

import com.learn.ms.event.common.mapper.BookingMapper;
import com.learn.ms.event.common.utils.DateTimeUtils;
import com.learn.ms.event.core.UniqueIdGenerationService;
import com.learn.ms.event.core.domain.enums.FeatureCode;
import com.learn.ms.event.core.domain.enums.NotificationType;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import com.learn.ms.event.core.domain.enums.TransactionStatus;
import com.learn.ms.event.core.domain.event.NotificationTemplateEvent;
import com.learn.ms.event.core.domain.event.PaymentRequest;
import com.learn.ms.event.core.domain.event.PostProcessingEvent;
import com.learn.ms.event.core.domain.exceptions.TicketNotAvailableException;
import com.learn.ms.event.core.domain.model.PaymentDetails;
import com.learn.ms.event.core.domain.request.BookingRequest;
import com.learn.ms.event.core.domain.response.BookingResponse;
import com.learn.ms.event.core.service.BaseService;
import com.learn.ms.event.core.service.BookingService;
import com.learn.ms.event.data.entity.BookingHistory;
import com.learn.ms.event.data.entity.Ticket;
import com.learn.ms.event.data.repository.BookingHistoryRepository;
import com.learn.ms.event.data.repository.TicketRepository;
import com.learn.ms.event.presenter.producer.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl extends BaseService implements BookingService {
    private static final int BOOKING_TIMEOUT = 5;
    private final ProducerService producerService;
    private final TicketRepository ticketRepository;
    private final UniqueIdGenerationService uniqueIdGenerationService;
    private final BookingHistoryRepository bookingHistoryRepository;
    private final BookingMapper bookingHistoryMapper;

    @Override
    public BookingResponse bookTicket(BookingRequest request) {
        Long ticketId = request.getTicketId();
        String ticketKey = "ticket:" + ticketId;

        checkTicketAvailability(ticketKey);

        reserveTicket(ticketKey);

        Ticket ticketEntity = getTicketEntity(ticketId);

        String pnrNumber = uniqueIdGenerationService.generateUniquePNR("PNR-");
        String transactionId = uniqueIdGenerationService.generateSequentialId();

        processPayment(request, ticketEntity, transactionId);

        BookingHistory bookingHistory = saveBookingHistory(request, pnrNumber, transactionId);

        return bookingHistoryMapper.mapEntityToResponse(bookingHistory);
    }

    private void checkTicketAvailability(String ticketKey) {
        String currentStatus = (String) redisTemplate.opsForHash().get(ticketKey, STATUS);
        if (TicketStatus.BOOKING_IN_PROGRESS.name().equals(currentStatus)) {
            throw new TicketNotAvailableException("Ticket is already processed by another user");
        }
    }

    private void reserveTicket(String ticketKey) {
        redisTemplate.opsForHash().put(ticketKey, "status", TicketStatus.BOOKING_IN_PROGRESS.name());
        redisTemplate.expire(ticketKey, BOOKING_TIMEOUT, TimeUnit.MINUTES);
    }

    private Ticket getTicketEntity(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotAvailableException("Ticket not found"));
    }

    private void processPayment(BookingRequest request, Ticket ticketEntity, String transactionId) {
        PaymentRequest paymentRequest = createPaymentRequest(request, ticketEntity);
        paymentRequest.setTransactionId(transactionId);
        producerService.producePaymentInitiationEvent(paymentRequest, getCorrelationId());
    }

    private BookingHistory saveBookingHistory(BookingRequest request, String pnrNumber, String transactionId) {
        BookingHistory bookingHistory = bookingHistoryMapper.mapRequestToEntity(request);
        bookingHistory.setPnrNumber(pnrNumber);
        bookingHistory.setTransactionId(transactionId);
        bookingHistory.setStatus(TicketStatus.BOOKING_IN_PROGRESS);
        bookingHistory.setBookingTime(getCurrentDate());
        bookingHistory.setPaymentStatus(TransactionStatus.INITIATED.name());
        bookingHistoryRepository.save(bookingHistory);
        return bookingHistory;
    }


    private PaymentRequest createPaymentRequest(BookingRequest request, Ticket ticket) {
        return PaymentRequest.builder()
                .paymentSourceType(request.getPaymentDetails().getPaymentSourceType())
                .amount(ticket.getPrice())
                .paymentDetails(convertPaymentDetailsToMap(request.getPaymentDetails()))
                .build();
    }


    private Map<String, Object> convertPaymentDetailsToMap(PaymentDetails paymentDetails) {
        Map<String, Object> paymentDetailsMap = new HashMap<>();
        paymentDetailsMap.put("paymentSourceType", paymentDetails.getPaymentSourceType());

        switch (paymentDetails.getPaymentSourceType()) {
            case ACCOUNT:
                paymentDetailsMap.put("accountNumber", paymentDetails.getAccountNumber());
                break;
            case CARD:
                paymentDetailsMap.put("cardNumber", paymentDetails.getCardNumber());
                paymentDetailsMap.put("cardHolderName", paymentDetails.getCardHolderName());
                paymentDetailsMap.put("expiryDate", paymentDetails.getExpiryDate());
                break;
            case MFS:
                paymentDetailsMap.put("mobileNumber", paymentDetails.getMobileNumber());
                break;
        }

        return paymentDetailsMap;
    }


    @Override
    public void processBooking(PostProcessingEvent postProcessingEvent) {
        BookingHistory history = bookingHistoryRepository.findByTransactionId(postProcessingEvent.getTransactionId());
        if (history == null) {
            throw new IllegalArgumentException("Booking history not found for transaction ID: " + postProcessingEvent.getTransactionId());
        }

        String ticketKey = "ticket:" + history.getTicketId();
        Long ttl = redisTemplate.getExpire(ticketKey, TimeUnit.MILLISECONDS);

        if (isTicketExpired(ttl)) {
            producerService.publishToDeadLetterQueue(postProcessingEvent);
            return;
        }

        TicketStatus newStatus = determineNewStatus(postProcessingEvent.getTransactionStatus());
        updateTicketStatus(ticketKey, newStatus);
        updateTicketEntity(history.getTicketId(), newStatus);

        updateBookingHistory(history, postProcessingEvent, newStatus);

        // publish event to notify the user
        producerService.produceNotificationEvent(getNotificationTemplateEvent(history), getCorrelationId());
    }


    private NotificationTemplateEvent getNotificationTemplateEvent(BookingHistory history) {
        NotificationTemplateEvent event = new NotificationTemplateEvent();
        event.setNotificationTypes(List.of(NotificationType.EMAIL));
        event.setToEmailList(List.of(history.getEmail()));
        event.setFeatureCode(FeatureCode.BOOKING_EVENT.getCode());

        Map<String, Object> additionalFields = new HashMap<>();

        additionalFields.put("emailSubject", "Ticket Booking Confirmation");
        additionalFields.put("name", history.getUsername());
        additionalFields.put("pnr", history.getPnrNumber());
        additionalFields.put("bookingTime", DateTimeUtils.formatDate(history.getBookingTime()));
        additionalFields.put("status", history.getStatus());
        additionalFields.put("category", history.getCategory());
        additionalFields.put("price", history.getPrice());
        additionalFields.put("transactionId", history.getTransactionId());

        additionalFields.put("accountNumber", history.getAccountNumber());

        event.setAdditionalFields(additionalFields);


        return event;
    }

    private boolean isTicketExpired(Long ttl) {
        return ttl == null || ttl <= 0;
    }

    private TicketStatus determineNewStatus(TransactionStatus transactionStatus) {
        return transactionStatus == TransactionStatus.SUCCESS ? TicketStatus.BOOKED : TicketStatus.AVAILABLE;
    }

    private void updateTicketStatus(String ticketKey, TicketStatus newStatus) {
        redisTemplate.opsForHash().put(ticketKey, "status", newStatus.name());
    }

    private void updateTicketEntity(Long ticketId, TicketStatus newStatus) {
        ticketRepository.findById(ticketId).ifPresent(ticket -> {
            ticket.setStatus(newStatus);
            ticketRepository.save(ticket);
        });
    }

    private void updateBookingHistory(BookingHistory history, PostProcessingEvent postProcessingEvent, TicketStatus newStatus) {
        history.setStatus(newStatus);
        history.setPaymentStatus(postProcessingEvent.getTransactionStatus().name());
        history.setAccountNumber(postProcessingEvent.getPaymentDetails().get("accountNumber"));
        bookingHistoryRepository.save(history);
    }
}
