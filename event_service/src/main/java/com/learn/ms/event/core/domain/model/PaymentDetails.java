package com.learn.ms.event.core.domain.model;

import com.learn.ms.event.core.domain.enums.PaymentSourceType;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentDetails {
    private PaymentSourceType paymentSourceType;
    private String accountNumber;


    private String cardNumber;
    private String cardHolderName;

    @Pattern(regexp = "^\\d{2}/\\d{2}$", message = "Expiry date is invalid")
    private String expiryDate;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Mobile number is invalid")
    private String mobileNumber;

}
