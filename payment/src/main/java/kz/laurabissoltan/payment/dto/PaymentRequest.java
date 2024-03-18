package kz.laurabissoltan.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotBlank(message = "Card number cannot be empty")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be exactly 16 digits")
    String cardNumber;
}
