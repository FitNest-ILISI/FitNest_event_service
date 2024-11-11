package org.ilisi.event.model;
import org.locationtech.jts.geom.Point;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Location {
    private String locationName;
    private double latitude;
    private double longitude;
    private Map<String, Double> coordinatesAsMap;

    // Getters et Setters
}

