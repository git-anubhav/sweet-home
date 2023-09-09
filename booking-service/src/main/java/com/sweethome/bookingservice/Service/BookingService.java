package com.sweethome.bookingservice.Service;

import com.sweethome.bookingservice.DTO.BookingRequestDTO;
import com.sweethome.bookingservice.DTO.PaymentRequestDTO;
import com.sweethome.bookingservice.Entity.BookingInfoEntity;
import com.sweethome.bookingservice.Exception.BookingServiceException;
import com.sweethome.bookingservice.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingInfoEntity create(final BookingRequestDTO bookingRequestDTO) {
        final BookingInfoEntity bookingInfo = new BookingInfoEntity();
        mapToEntity(bookingRequestDTO, bookingInfo);
        return bookingRepository.save(bookingInfo);
    }

    public BookingInfoEntity processPayment(int bookingId, PaymentRequestDTO paymentRequestDTO) {
        BookingInfoEntity bookingInfo = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingServiceException("Invalid Booking Id", 400));

        if (!isValidPaymentMode(paymentRequestDTO.getPaymentMode())) {
            throw new BookingServiceException("Invalid mode of payment", 400);
        }

        if (paymentRequestDTO.getPaymentMode().equals("UPI")) {
            if (paymentRequestDTO.getCardNumber().trim().length() != 0) {
                throw new BookingServiceException("Invalid payment details: Card number provided for UPI payment", 400);
            }
        } else if (paymentRequestDTO.getPaymentMode().equals("CARD")) {
            if (paymentRequestDTO.getUpiId().trim().length() != 0) {
                throw new BookingServiceException("Invalid payment details: UPI ID provided for Card payment", 400);
            }
        }

        // process transaction with payment service
        int transactionId = processTransaction(paymentRequestDTO);
        bookingInfo.setTransactionId(transactionId);

        // save booking with new transactionId and print booking details
        BookingInfoEntity savedBooking = bookingRepository.save(bookingInfo);

        String message = "Booking confirmed for user with Aadhaar number: "
                + savedBooking.getAadharNumber()
                + "    |    "
                + "Here are the booking details:    " + savedBooking;

        System.out.println(message);

        return savedBooking;
    }

    private Integer processTransaction(PaymentRequestDTO paymentRequestDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String paymentServiceUrl = "http://localhost:8083/transaction";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentRequestDTO> request = new HttpEntity<>(paymentRequestDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(paymentServiceUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return Integer.parseInt(Objects.requireNonNull(response.getBody()));
        } else {
            throw new BookingServiceException("Transaction was unsuccessful", 500);
        }
    }

    private void mapToEntity(final BookingRequestDTO bookingRequestDTO, final BookingInfoEntity bookingInfo) {
        Map<String, Object> dateData = getDateData(bookingRequestDTO.getFromDate(), bookingRequestDTO.getToDate());
        String roomNumbers = generateRandomRoomNumbers(bookingRequestDTO.getNumOfRooms());

        bookingInfo.setFromDate((Date) dateData.get("fromDate"));
        bookingInfo.setToDate((Date) dateData.get("toDate"));
        bookingInfo.setAadharNumber(bookingRequestDTO.getAadharNumber());
        bookingInfo.setNumOfRooms(bookingRequestDTO.getNumOfRooms());
        bookingInfo.setRoomNumbers(roomNumbers);
        bookingInfo.setRoomPrice(bookingRequestDTO.getNumOfRooms() * 1000 * (int) dateData.get("days"));
        bookingInfo.setTransactionId(0);
        bookingInfo.setBookedOn(new Date());
    }

    private String generateRandomRoomNumbers(int numOfRooms) {
        Random random = new Random();
        StringBuilder roomNumbers = new StringBuilder();

        for (int i = 0; i < numOfRooms; i++) {
            if (i > 0) {
                roomNumbers.append(",");
            }
            roomNumbers.append(random.nextInt(100) + 1); // Generate random room numbers between 1 and 100
        }

        return roomNumbers.toString();
    }

    private Map<String, Object> getDateData(String fromDateReq, String toDateReq) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = dateFormat.parse(fromDateReq);
            Date toDate = dateFormat.parse(toDateReq);

            long timeDifference = toDate.getTime() - fromDate.getTime();
            int days = (int) TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS);

            Map<String, Object> values = new HashMap<>();
            values.put("fromDate", fromDate);
            values.put("toDate", toDate);
            values.put("days", days);

            return values;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd format for fromDate and toDate.");
        }
    }

    private boolean isValidPaymentMode(String paymentMode) {
        return paymentMode.equals("UPI") || paymentMode.equals("CARD");
    }
}










