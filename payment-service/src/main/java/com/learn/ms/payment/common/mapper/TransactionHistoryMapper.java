package com.learn.ms.payment.common.mapper;


import com.learn.ms.payment.core.domain.model.PaymentRequest;
import com.learn.ms.payment.data.entity.TransactionHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionHistoryMapper {

    TransactionHistory mapToEntity(PaymentRequest request);

}
