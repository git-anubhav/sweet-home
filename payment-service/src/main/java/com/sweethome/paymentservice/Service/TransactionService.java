package com.sweethome.paymentservice.Service;

import com.sweethome.paymentservice.DTO.TransactionRequestDTO;
import com.sweethome.paymentservice.DTO.TransactionResponseDTO;
import com.sweethome.paymentservice.Entity.TransactionDetailsEntity;
import com.sweethome.paymentservice.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Integer create(final TransactionRequestDTO transactionRequestDTO) {
        final TransactionDetailsEntity transactionDetails = new TransactionDetailsEntity();
        mapToEntity(transactionRequestDTO, transactionDetails);

        TransactionDetailsEntity savedTransaction = transactionRepository.save(transactionDetails);
        return savedTransaction.getTransactionId();
    }

    public TransactionResponseDTO getById(Integer transactionId) {
        final TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();

        TransactionDetailsEntity transactionDetails = transactionRepository.findById(transactionId).orElse(null);
        if (transactionDetails != null) {
            mapToDTO(transactionDetails, transactionResponseDTO);
            return transactionResponseDTO;
        }
        return null;
    }

    private void mapToEntity(final TransactionRequestDTO transactionRequestDTO, final TransactionDetailsEntity transactionDetails) {
        transactionDetails.setPaymentMode(transactionRequestDTO.getPaymentMode());
        transactionDetails.setBookingId(transactionRequestDTO.getBookingId());
        transactionDetails.setUpiId(transactionRequestDTO.getUpiId());
        transactionDetails.setCardNumber(transactionRequestDTO.getCardNumber());
    }

    private void mapToDTO(final TransactionDetailsEntity transactionDetailsEntity, final TransactionResponseDTO transactionResponseDTO) {
        transactionResponseDTO.setId(transactionDetailsEntity.getTransactionId());
        transactionResponseDTO.setPaymentMode(transactionDetailsEntity.getPaymentMode());
        transactionResponseDTO.setBookingId(transactionDetailsEntity.getBookingId());
        transactionResponseDTO.setUpiId(transactionDetailsEntity.getUpiId());
        transactionResponseDTO.setCardNumber(transactionDetailsEntity.getCardNumber());
    }
}