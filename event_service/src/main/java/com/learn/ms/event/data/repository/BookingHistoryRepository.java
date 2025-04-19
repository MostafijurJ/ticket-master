package com.learn.ms.event.data.repository;

import com.learn.ms.event.data.entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Long> {

    BookingHistory findByTransactionId(String transactionId);

}
