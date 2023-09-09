package com.sweethome.paymentservice.DTO;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    private String paymentMode;
    private int bookingId;
    private String upiId;
    private String cardNumber;
}
