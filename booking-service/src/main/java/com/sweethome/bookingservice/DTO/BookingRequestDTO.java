package com.sweethome.bookingservice.DTO;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private String fromDate;
    private String toDate;
    private String aadharNumber;
    private int numOfRooms;
}