package org.ilisi.event.repository;
import org.ilisi.event.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByIdCoordinator(Long userId);

}
