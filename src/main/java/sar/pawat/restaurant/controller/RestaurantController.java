package sar.pawat.restaurant.controller;


import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;
import sar.pawat.restaurant.entity.Restaurant;
import sar.pawat.restaurant.service.RestaurantService;

import java.util.List;
import java.util.UUID;


@RestController
public class RestaurantController {
    private final RestaurantService service;

    @Autowired
    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return service.getAll();
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant getRestaurantById(@PathVariable UUID id) {
        return service.getRestaurantById(id).orElse(null);
    }

    @PutMapping("/restaurants")
    public Restaurant update(@RequestBody Restaurant restaurant) {
        return service.update(restaurant);
    }

    @GetMapping("/restaurants/name/{name}")
    public Restaurant getRestaurantByName(@PathVariable String name) {
        return service.getRestaurantByName(name);
    }

    @GetMapping("/restaurants/location/{location}")
    public List<Restaurant> getRestaurantByLocation(@PathVariable String location) {
        return service.getRestaurantByLocation(location);
    }

    @DeleteMapping("/restaurants/{id}")
    public Restaurant delete(@PathVariable UUID id) {
        return service.delete(id);
    }

    @PostMapping("/restaurants")
    public Restaurant create(@RequestBody Restaurant restaurant) {
        return service.create(restaurant);
    }
}
