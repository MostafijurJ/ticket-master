package com.learn.ms.event.data.repository;

import com.learn.ms.event.core.domain.enums.EventStatus;
import com.learn.ms.event.data.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByOrderByIdDesc(Pageable pageable);

    List<Event> findByEventDateBeforeAndStatus(Date dateTime, EventStatus status);

}
