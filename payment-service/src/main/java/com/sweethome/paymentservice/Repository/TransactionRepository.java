package com.sweethome.paymentservice.Repository;

import com.sweethome.paymentservice.Entity.TransactionDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetailsEntity, Integer> {
}
