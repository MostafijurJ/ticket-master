package com.learn.ms.payment.core.service;

import com.learn.ms.payment.core.domain.event.PostProcessingEvent;
import com.learn.ms.payment.core.domain.model.PaymentRequest;
import com.learn.ms.payment.core.domain.model.PaymentResponse;
import com.learn.ms.payment.presenter.producer.ProducerService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessor extends BaseService {
    private final PaymentServiceFactory paymentServiceFactory;
    private final ProducerService producerService;

    public void processPayment(PaymentRequest paymentRequest, String correlationId) {
        try {
            PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentRequest.getPaymentSourceType());
            PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);
            handlePaymentResponse(paymentResponse, correlationId);
        } catch (IllegalArgumentException e) {
            logger.error("Payment processing failed: {}", e.getMessage());
            throw new BadRequestException("Invalid payment type -> " + paymentRequest.getPaymentSourceType().name());
        }
    }

    private void handlePaymentResponse(PaymentResponse paymentResponse, String correlationId) {
        logger.trace("Payment processed successfully: {}", paymentResponse);
        producerService.producePostProcessingEvent(getPostProcessingEvent(paymentResponse), correlationId);
    }


    private PostProcessingEvent getPostProcessingEvent(PaymentResponse paymentResponse) {
        return PostProcessingEvent.builder()
                .paymentSourceType(paymentResponse.getPaymentSourceType())
                .transactionStatus(paymentResponse.getTransactionStatus())
                .transactionId(paymentResponse.getTransactionId())
                .amount(paymentResponse.getAmount())
                .build();
    }
}