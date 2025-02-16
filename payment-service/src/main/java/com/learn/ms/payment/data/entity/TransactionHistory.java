package com.learn.ms.payment.data.entity;

import com.learn.ms.payment.core.domain.enums.PaymentSourceType;
import com.learn.ms.payment.core.domain.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "TRANSACTION_HISTORY")
public class TransactionHistory extends BaseEntity {

    @Column(name = "TRANSACTION_ID", nullable = false)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_SOURCE_TYPE", nullable = false)
    private PaymentSourceType paymentSourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_STATUS", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "TRANSACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "TRANSACTION_DESCRIPTION")
    private String transactionDescription;


}
