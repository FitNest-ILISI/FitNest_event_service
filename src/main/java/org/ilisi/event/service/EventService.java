package org.ilisi.event.service;

import org.ilisi.event.clients.GeolocationFeignClient;
import org.ilisi.event.entities.Event;
import org.ilisi.event.entities.SportCategory;
import org.ilisi.event.exceptions.LocationNotFoundException;
import org.ilisi.event.exceptions.RouteNotFoundException;
import org.ilisi.event.exceptions.SportCategoryNotFoundException;
import org.ilisi.event.repository.EventRepository;
import org.ilisi.event.repository.SportCategoryRepository;
import org.ilisi.event.model.Location;
import org.ilisi.event.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SportCategoryRepository sportCategoryRepository;
    private final GeolocationFeignClient geoFeignClient; // OpenFeign Client pour la géolocalisation

    @Autowired
    public EventService(EventRepository eventRepository, SportCategoryRepository sportCategoryRepository, GeolocationFeignClient geoFeignClient) {
        this.eventRepository = eventRepository;
        this.sportCategoryRepository = sportCategoryRepository;
        this.geoFeignClient = geoFeignClient;
    }

    public Event createEvent(Event event) {

        Optional<SportCategory> sportCategory = sportCategoryRepository.findById(event.getSportCategory().getId());
        if (sportCategory.isEmpty()) {
            throw new SportCategoryNotFoundException("Sport category not found.");
        }
        event.setSportCategory(sportCategory.get());

        if (sportCategory.get().isRequiresRoute())
        {
            if (event.getRouteId() == null) {
                throw new IllegalArgumentException("Route ID is required for this event.");
            }
            Route route = geoFeignClient.getRouteById(event.getRouteId());
            if (route == null) {
                throw new RouteNotFoundException("Route not found.");
            }
            event.setRoute(route);
        }
        else
        {
            Location location = geoFeignClient.getLocationById(event.getLocationId());
             if (location == null)
                   throw new IllegalArgumentException("Location not found.");
        event.setLocation(location);
        }
        return eventRepository.save(event);
    }


    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    public List<Event> getEventsByCategoryName(String categoryName) {
        return eventRepository.findBySportCategoryName(categoryName);
    }

    public List<Event> getEventsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByStartDateBetween(startDate, endDate);
    }

    public List<Event> getEventsForToday() {
        return eventRepository.findEventsForToday();
    }

    public List<Event> getEventsForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return eventRepository.findEventsForTomorrow(tomorrow);
    }
    public List<Event> getEventsThisWeek() {
        LocalDate oneWeekLater = LocalDate.now().plusDays(7);
        return eventRepository.findEventsForThisWeek(oneWeekLater);
    }

    public List<Event> getEventsAfterThisWeek() {
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        return eventRepository.findEventsAfterThisWeek(nextMonday);
    }
    public List<Event> getEventsBySportCategory(String categoryName) {
        return eventRepository.findBySportCategoryName(categoryName);
    }

    public List<Event> findEventsByPartOfDay(String partOfDay) {
        LocalTime startTime, endTime;
        List<Event> events = new ArrayList<>();

        switch (partOfDay.toLowerCase()) {
            case "morning":
                startTime = LocalTime.of(5, 0);
                endTime = LocalTime.of(11, 59);
                events = eventRepository.findByTimeRange(startTime, endTime);
                break;
            case "afternoon":
                startTime = LocalTime.of(12, 0);
                endTime = LocalTime.of(16, 59);
                events = eventRepository.findByTimeRange(startTime, endTime);
                break;
            case "evening":
                startTime = LocalTime.of(17, 0);
                endTime = LocalTime.of(20, 59);
                events = eventRepository.findByTimeRange(startTime, endTime);
                break;
            case "night":
                events.addAll(eventRepository.findByTimeRange(LocalTime.of(21, 0), LocalTime.of(23, 59)));
                events.addAll(eventRepository.findByTimeRange(LocalTime.of(0, 0), LocalTime.of(4, 59)));
                break;
            default:
                throw new IllegalArgumentException("Invalid part of day: " + partOfDay);
        }

        return events;
    }
    public List<Event> getAllEventsWithDetails() {
        List<Event> events = eventRepository.findAll();

        // Charger les détails de chaque événement
        events.forEach(event -> {
            if (event.getLocationId() != null) {
                try {
                    Location location = geoFeignClient.getLocationById(event.getLocationId());
                    event.setLocation(location);
                } catch (Exception e) {
                    System.out.println("Erreur lors de la récupération de la localisation pour l'événement " + event.getId() + ": " + e.getMessage());
                }
            }

            if (event.getRouteId() != null) {
                try {
                    Route route = geoFeignClient.getRouteById(event.getRouteId());
                    event.setRoute(route);
                } catch (Exception e) {
                    System.out.println("Erreur lors de la récupération de la route pour l'événement " + event.getId() + ": " + e.getMessage());
                }
            }
        });

        return events;
    }


    public Event getEventWithDetails(Long eventId) {
        // Récupération de l'événement
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        // Récupérer et ajouter la localisation si locationId existe
        if (event.getLocationId() != null) {
            try {
                Location location = geoFeignClient.getLocationById(event.getLocationId());
                event.setLocation(location);
            } catch (Exception e) {
                // Log l'erreur ou ajouter une gestion de fallback
                System.out.println("Erreur lors de la récupération de la localisation: " + e.getMessage());
            }
        }

        // Récupérer et ajouter la route si routeId existe
        if (event.getRouteId() != null) {
            try {
                Route route = geoFeignClient.getRouteById(event.getRouteId());
                event.setRoute(route);
            } catch (Exception e) {
                // Log l'erreur ou ajouter une gestion de fallback
                System.out.println("Erreur lors de la récupération de la route: " + e.getMessage());
            }
        }

        return event;
    }

}
