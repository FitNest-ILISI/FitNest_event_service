package org.ilisi.event.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.IOException;
import java.util.List;

public class LineStringDeserializer extends JsonDeserializer<LineString> {

    @Override
    public LineString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Lire les coordonnées du JSON
        List<List<Double>> coordinates = p.readValueAs(List.class);

        // Convertir les coordonnées en tableau de Coordinate
        Coordinate[] coords = new Coordinate[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            List<Double> point = coordinates.get(i);
            coords[i] = new Coordinate(point.get(0), point.get(1)); // x, y -> longitude, latitude
        }

        // Créer un LineString avec ces coordonnées
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createLineString(coords);
    }
}
