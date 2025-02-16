package com.learn.ms.event.core.domain.event;


import com.learn.ms.event.core.domain.enums.PaymentSourceType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@Accessors(chain = true)
public class PaymentRequest {
    private String transactionId;
    private PaymentSourceType paymentSourceType;
    private BigDecimal amount;
    private String accountNumber;
    private Map<String, Object> paymentDetails;
}
