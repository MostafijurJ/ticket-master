package com.learn.ms.event.data.repository;

import com.learn.ms.event.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByEventIdAndActiveTrue(Long eventId);

}
