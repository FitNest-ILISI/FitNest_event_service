package org.ilisi.event.repository;

import org.ilisi.event.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findBySportCategoryName(String categoryName);

    List<Event> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM Event e WHERE e.startDate = CURRENT_DATE")
    List<Event> findEventsForToday();

    @Query("SELECT e FROM Event e WHERE e.startDate = :tomorrow")
    List<Event> findEventsForTomorrow(@Param("tomorrow") LocalDate tomorrow);

    @Query("SELECT e FROM Event e WHERE e.startDate >= CURRENT_DATE AND e.startDate < :endOfWeek")
    List<Event> findEventsForThisWeek(@Param("endOfWeek") LocalDate endOfWeek);

    @Query("SELECT e FROM Event e WHERE e.startDate >= :date")
    List<Event> findEventsAfterThisWeek(@Param("date") LocalDate date);

    @Query("SELECT e FROM Event e WHERE e.startTime BETWEEN :startTime AND :endTime")
    List<Event> findByTimeRange(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    List<Event>  findByIdCoordinator(Long userId);
}
