package com.learn.ms.payment.core.domain.model;


import com.learn.ms.payment.core.domain.enums.PaymentSourceType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PaymentRequest {
    private String transactionId;
    private PaymentSourceType paymentSourceType;
    private BigDecimal amount;
    private String accountNumber;
}
