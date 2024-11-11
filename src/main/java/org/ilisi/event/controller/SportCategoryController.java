package org.ilisi.event.controller;

import org.ilisi.event.entities.Event;
import org.ilisi.event.entities.SportCategory;
import org.ilisi.event.service.EventService;
import org.ilisi.event.service.SportCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class SportCategoryController {

    private final SportCategoryService sportCategoryService;
    private final EventService eventService;

    @Autowired
    public SportCategoryController(SportCategoryService sportCategoryService, EventService eventService) {
        this.sportCategoryService = sportCategoryService;
        this.eventService = eventService;
    }

    @GetMapping("getCategories")
    public List<SportCategory> getAllCategories() {
        return sportCategoryService.getAllCategories();
    }

    @GetMapping("/events/{categoryName}")
    public List<Event> getEventsByCategoryName(@PathVariable String categoryName) {
        return eventService.getEventsByCategoryName(categoryName);
    }

}
