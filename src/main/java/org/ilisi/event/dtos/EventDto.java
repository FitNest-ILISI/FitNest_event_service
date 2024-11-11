package org.ilisi.event.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private int maxParticipants;
    private int currentNumParticipants;
    private String imagePath;
    private Long sportCategoryId;
    private String sportCategoryName;
    private String cityName; // Nouveau champ pour le nom de la ville
    private double latitude;
    private double longitude;
    private Long LocationId;
    private Long RouteId;
    private List<double[]> routeCoordinates;


}