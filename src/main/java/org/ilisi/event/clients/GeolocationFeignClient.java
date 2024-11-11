package org.ilisi.event.clients;

import org.ilisi.event.model.Location;
import org.ilisi.event.model.Route;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "GEOLOCALISATION-SERVICE")
public interface GeolocationFeignClient {

    @GetMapping("/locations/{locationId}")
    Location getLocationById(@PathVariable("locationId") Long locationId);


    @GetMapping("/routes/{routeId}")
    Route getRouteById(@PathVariable("routeId") Long routeId);

    @PostMapping("/routes")
    Route createRoute(@RequestBody Route route);
}
