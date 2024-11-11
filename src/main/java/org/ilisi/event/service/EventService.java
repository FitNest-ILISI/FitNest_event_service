package org.ilisi.event.service;

import org.ilisi.event.clients.GeolocationFeignClient;
import org.ilisi.event.entities.Event;
import org.ilisi.event.entities.SportCategory;
import org.ilisi.event.repository.EventRepository;
import org.ilisi.event.repository.SportCategoryRepository;
import org.ilisi.event.model.Location;
import org.ilisi.event.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Récupérer la catégorie de sport associée
        Optional<SportCategory> sportCategory = sportCategoryRepository.findById(event.getSportCategory().getId());
        if (sportCategory.isEmpty()) {
            throw new IllegalArgumentException("Sport category not found.");
        }
        event.setSportCategory(sportCategory.get());

        // Vérifier si l'événement nécessite une route ou une localisation
        if (sportCategory.get().isRequiresRoute()) {
            Route route = geoFeignClient.getRouteById(event.getRouteId());
            if (route == null) {
                throw new IllegalArgumentException("Route not found.");
            }
            event.setRoute(route);
        } else {
            Location location = geoFeignClient.getLocationById(event.getLocationId());
            if (location == null) {
                throw new IllegalArgumentException("Location not found.");
            }
            // Assigner la location récupérée à l'événement
            event.setLocation(location);
        }

        // Enregistrer l'événement en base de données
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

    public List<Event> getEventsByIdCoordinator(Long userId) {
        return eventRepository.findByIdCoordinator(userId);
    }

}
