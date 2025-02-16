package com.learn.ms.payment.core.service;

import com.learn.ms.payment.core.domain.model.PaymentRequest;
import com.learn.ms.payment.core.domain.model.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
}