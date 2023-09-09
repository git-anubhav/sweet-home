package com.sweethome.paymentservice.Controller;

import com.sweethome.paymentservice.DTO.TransactionRequestDTO;
import com.sweethome.paymentservice.DTO.TransactionResponseDTO;
import com.sweethome.paymentservice.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Integer> createTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        return new ResponseEntity<Integer>(transactionService.create(transactionRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable Integer transactionId) {
        TransactionResponseDTO transactionDTO = transactionService.getById(transactionId);

        if (transactionDTO != null) {
            return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
