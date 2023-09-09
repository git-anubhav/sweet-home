package com.sweethome.paymentservice.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transaction")
public class TransactionDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private int transactionId;

    @Column(name = "paymentMode")
    private String paymentMode;

    @Column(name = "bookingId", nullable = false)
    private int bookingId;

    @Column(name = "upiId")
    private String upiId;

    @Column(name = "cardNumber")
    private String cardNumber;
}
