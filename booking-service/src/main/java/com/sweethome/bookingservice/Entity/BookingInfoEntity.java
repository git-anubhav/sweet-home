package com.sweethome.bookingservice.Entity;

import lombok.Data;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Data
@Table(name = "booking")
public class BookingInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingId")
    private int bookingId;

    @Column(name = "fromDate")
    private Date fromDate;

    @Column(name = "toDate")
    private Date toDate;

    @Column(name = "aadharNumber")
    private String aadharNumber;

    @Column(name = "numOfRooms")
    private int numOfRooms;

    @Column(name = "roomNumbers")
    private String roomNumbers;

    @Column(name = "roomPrice", nullable = false)
    private int roomPrice;

    @Column(name = "transactionId", columnDefinition = "int default 0")
    private int transactionId;

    @Column(name = "bookedOn")
    private Date bookedOn;
}
