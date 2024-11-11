package org.ilisi.event.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

import java.util.List;

@Data
@NoArgsConstructor
public class Route {

    private Long id;
    private LineString path;

    public List<List<Double>> getCoordinatesFromPath() {
        if (this.path == null) {
            return null;
        }

        return List.of(this.path.getCoordinates()).stream()
                .map(coord -> List.of(coord.getY(), coord.getX())) // latitude (y), longitude (x)
                .toList();
    }
}
