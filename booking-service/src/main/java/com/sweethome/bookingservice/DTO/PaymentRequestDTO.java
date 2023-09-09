package com.sweethome.bookingservice.DTO;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String paymentMode;
    private int bookingId;
    private String upiId;
    private String cardNumber;
}
