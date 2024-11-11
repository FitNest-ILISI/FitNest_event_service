package org.ilisi.event.controller;

import org.ilisi.event.dtos.EventDto;
import org.ilisi.event.entities.Event;
import org.ilisi.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Endpoint pour créer un nouvel événement
    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            Event createdEvent = eventService.createEvent(event);
            return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all-details")
    public ResponseEntity<List<Event>> getAllEventsWithDetails() {
        List<Event> events = eventService.getAllEventsWithDetails();
        return ResponseEntity.ok(events);
    }

    // Fetch event with additional details
    @GetMapping("/{id}/details")
    public ResponseEntity<Event> getEventWithDetails(@PathVariable Long id) {
        Event event = eventService.getEventWithDetails(id);
        return ResponseEntity.ok(event);
    }

    // Fetch event with basic details
    @GetMapping("/{id}/basic")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint pour obtenir tous les événements
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // Endpoint pour supprimer un événement par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.getEventById(id).isPresent()) {
            eventService.deleteEvent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/between")
    public List<EventDto> getEventsBetweenDates(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDate startDateTime = LocalDate.parse(startDate);
            LocalDate endDateTime = LocalDate.parse(endDate);
            List<Event> events = eventService.getEventsBetweenDates(startDateTime, endDateTime);
            return events.stream().map(Event::toDto).collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format", e);
        }
    }

    @GetMapping("/filterByDate")
    public ResponseEntity<List<EventDto>> getEventsByDateFilter(@RequestParam("filter") String filter) {
        List<Event> events;
        switch (filter.toLowerCase()) {
            case "today":
                events = eventService.getEventsForToday();
                break;
            case "tomorrow":
                events = eventService.getEventsForTomorrow();
                break;
            case "thisweek":
                events = eventService.getEventsThisWeek();
                break;
            case "afterthisweek":
                events = eventService.getEventsAfterThisWeek();
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        List<EventDto> eventDtos = events.stream().map(Event::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(eventDtos);
    }

    @GetMapping("/filterByCategoryAndDate")
    public ResponseEntity<List<EventDto>> getEventsByCategoryAndDateFilter(
            @RequestParam("categoryName") String categoryName,
            @RequestParam("filter") String filter) {

        List<Event> eventsByCategory = eventService.getEventsBySportCategory(categoryName);
        List<Event> filteredEvents;
        switch (filter.toLowerCase()) {
            case "today":
                filteredEvents = eventService.getEventsForToday().stream()
                        .filter(eventsByCategory::contains)
                        .collect(Collectors.toList());
                break;
            case "tomorrow":
                filteredEvents = eventService.getEventsForTomorrow().stream()
                        .filter(eventsByCategory::contains)
                        .collect(Collectors.toList());
                break;
            case "thisweek":
                filteredEvents = eventService.getEventsThisWeek().stream()
                        .filter(eventsByCategory::contains)
                        .collect(Collectors.toList());
                break;
            case "afterthisweek":
                filteredEvents = eventService.getEventsAfterThisWeek().stream()
                        .filter(eventsByCategory::contains)
                        .collect(Collectors.toList());
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        List<EventDto> eventDtos = filteredEvents.stream()
                .map(Event::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDtos);
    }
    @GetMapping("/associated/{userid}")
    public ResponseEntity<List<Event>> findEventsByIdCoordinator(@PathVariable("userid") Long userid) {
        List<Event> events = eventService.getEventsByIdCoordinator(userid);
        return ResponseEntity.ok(events);
    }
}
