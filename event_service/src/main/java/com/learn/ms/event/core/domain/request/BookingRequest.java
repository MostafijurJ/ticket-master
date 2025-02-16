package com.learn.ms.event.core.domain.request;

import com.learn.ms.event.core.domain.model.PaymentDetails;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest implements Serializable {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phone;

    @NotNull(message = "event id is mandatory")
    private Long eventId;

    @NotNull(message = "ticket id is mandatory")
    private Long ticketId;

    @NotNull(message = "payment details is mandatory")
    private PaymentDetails paymentDetails;

}
