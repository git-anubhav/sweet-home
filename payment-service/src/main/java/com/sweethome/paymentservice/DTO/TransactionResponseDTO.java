package com.sweethome.paymentservice.DTO;

import lombok.Data;

@Data
public class TransactionResponseDTO {
    private int id;
    private String paymentMode;
    private int bookingId;
    private String upiId;
    private String cardNumber;
}
