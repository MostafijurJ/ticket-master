package com.learn.ms.payment.core.domain.model;


import com.learn.ms.payment.core.domain.enums.PaymentSourceType;
import com.learn.ms.payment.core.domain.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@Builder
public class PaymentResponse {
    private String transactionId;
    private PaymentSourceType paymentSourceType;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;
    private String accountNumber;
    private Date transactionDate;
    private String transactionDescription;
}
