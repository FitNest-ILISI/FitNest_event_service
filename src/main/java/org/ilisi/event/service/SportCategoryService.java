package org.ilisi.event.service;

import org.ilisi.event.entities.SportCategory;
import org.ilisi.event.repository.SportCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportCategoryService {

    private final SportCategoryRepository sportCategoryRepository;

    @Autowired
    public SportCategoryService(SportCategoryRepository sportCategoryRepository) {
        this.sportCategoryRepository = sportCategoryRepository;
    }

    public List<SportCategory> getAllCategories() {
        return sportCategoryRepository.findAll();
    }
}
