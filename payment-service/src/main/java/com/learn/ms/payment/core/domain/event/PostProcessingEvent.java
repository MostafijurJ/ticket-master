package com.learn.ms.payment.core.domain.event;

import com.learn.ms.payment.core.domain.enums.PaymentSourceType;
import com.learn.ms.payment.core.domain.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@Accessors(chain = true)
public class PostProcessingEvent {
    private String transactionId;
    private PaymentSourceType paymentSourceType;
    private TransactionStatus transactionStatus;
    private BigDecimal amount;
    private Map<String, String> paymentDetails;
}
