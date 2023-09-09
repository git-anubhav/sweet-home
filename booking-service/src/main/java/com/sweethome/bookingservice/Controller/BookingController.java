package com.sweethome.bookingservice.Controller;

import com.sweethome.bookingservice.DTO.BookingRequestDTO;
import com.sweethome.bookingservice.DTO.ErrorResponseDTO;
import com.sweethome.bookingservice.DTO.PaymentRequestDTO;
import com.sweethome.bookingservice.Entity.BookingInfoEntity;
import com.sweethome.bookingservice.Exception.BookingServiceException;
import com.sweethome.bookingservice.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingInfoEntity> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        return new ResponseEntity<>(bookingService.create(bookingRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/transaction")
    public ResponseEntity<?> processPayment(@PathVariable int bookingId, @RequestBody PaymentRequestDTO paymentRequestDto) {
        try {
            return new ResponseEntity<>(bookingService.processPayment(bookingId, paymentRequestDto), HttpStatus.OK);
        } catch (BookingServiceException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), e.getStatusCode());
            return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.getStatusCode()));
        }
    }
}

