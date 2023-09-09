package com.sweethome.bookingservice.Repository;

import com.sweethome.bookingservice.Entity.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingInfoEntity, Integer> {
}