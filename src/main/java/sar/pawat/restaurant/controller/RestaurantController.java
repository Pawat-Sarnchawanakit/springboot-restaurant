package sar.pawat.restaurant.controller;


import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;
import sar.pawat.restaurant.dto.RestaurantRequest;
import sar.pawat.restaurant.entity.Restaurant;
import sar.pawat.restaurant.service.RestaurantService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class RestaurantController {
    private final RestaurantService service;

    @Autowired
    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping("/restaurants")
    public Page<@NonNull Restaurant> getAllRestaurants(
            @RequestParam(value="offset", required = false) Integer offset,
            @RequestParam(value="pageSize", required = false) Integer pageSize,
            @RequestParam(value="sortBy", required = false) String sortBy
    ) {
        if(offset == null) offset = 0;
        if(pageSize == null) pageSize = 10;
        pageSize = Math.min(200, pageSize);
        if(sortBy == null) sortBy = "name";
        return service.getRestaurantsPage(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant getRestaurantById(@Valid @PathVariable UUID id) {
        return service.getRestaurantById(id);
    }

    @PutMapping("/restaurants")
    public Restaurant update(@Valid @RequestBody Restaurant restaurant) {
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
    public Restaurant delete(@Valid @PathVariable UUID id) {
        return service.delete(id);
    }

    @PostMapping("/restaurants")
    public Restaurant create(@Valid @RequestBody RestaurantRequest restaurant) {
        return service.create(restaurant);
    }
}
