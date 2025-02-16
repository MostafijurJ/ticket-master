package com.learn.ms.payment.core.service.impl;

import com.learn.ms.payment.common.mapper.TransactionHistoryMapper;
import com.learn.ms.payment.core.domain.enums.PaymentSourceType;
import com.learn.ms.payment.core.domain.enums.TransactionStatus;
import com.learn.ms.payment.core.domain.model.PaymentRequest;
import com.learn.ms.payment.core.domain.model.PaymentResponse;
import com.learn.ms.payment.core.service.BaseService;
import com.learn.ms.payment.core.service.PaymentService;
import com.learn.ms.payment.data.entity.TransactionHistory;
import com.learn.ms.payment.data.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MFSPaymentService extends BaseService implements PaymentService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryMapper mapper;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        logger.trace("Processing payment for MFS: {}", request);

        // TODO external MFS payment related logic will be implemented here
        TransactionHistory transactionHistory = mapper.mapToEntity(request);

        transactionHistoryRepository.save(transactionHistory);
        return PaymentResponse.builder()
                .transactionStatus(TransactionStatus.SUCCESS)
                .paymentSourceType(PaymentSourceType.MFS)
                .transactionId(request.getTransactionId())
                .amount(request.getAmount())
                .accountNumber(request.getAccountNumber())
                .transactionDate(new Date())
                .transactionDescription("Payment processed successfully")
                .build();
    }
}