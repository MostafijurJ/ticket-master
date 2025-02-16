package com.learn.ms.payment.core.service;

import com.learn.ms.payment.core.domain.enums.PaymentSourceType;
import com.learn.ms.payment.core.service.impl.AccountPaymentService;
import com.learn.ms.payment.core.service.impl.CardPaymentService;
import com.learn.ms.payment.core.service.impl.MFSPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PaymentServiceFactory {
    private final Map<PaymentSourceType, PaymentService> paymentServiceMap;

    @Autowired
    public PaymentServiceFactory(List<PaymentService> paymentServices) {
        this.paymentServiceMap = paymentServices.stream()
                .collect(Collectors.toMap(
                        service -> getPaymentType(service.getClass()),
                        service -> service
                ));
    }

    public PaymentService getPaymentService(PaymentSourceType paymentType) {
        return Optional.ofNullable(paymentServiceMap.get(paymentType))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported payment type: " + paymentType));
    }

    private PaymentSourceType getPaymentType(Class<? extends PaymentService> clazz) {
        if (clazz.equals(AccountPaymentService.class)) return PaymentSourceType.ACCOUNT;
        if (clazz.equals(CardPaymentService.class)) return PaymentSourceType.CARD;
        if (clazz.equals(MFSPaymentService.class)) return PaymentSourceType.MFS;
        throw new IllegalArgumentException("Unknown payment service: " + clazz.getSimpleName());
    }
}

