package com.eventor.repository;

import com.eventor.model.Event;
import com.eventor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByPublished(boolean published);
    List<Event> findByTitleContaining(String title);
    List<Event> findByCreator(User creator);
}
