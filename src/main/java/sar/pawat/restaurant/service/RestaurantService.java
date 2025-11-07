package sar.pawat.restaurant.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sar.pawat.restaurant.dto.RestaurantRequest;
import sar.pawat.restaurant.entity.Restaurant;
import sar.pawat.restaurant.repository.RestaurantRepository;


import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    private static final EntityNotFoundException RESTAURANT_NOT_FOUND_EXCEPTION = new EntityNotFoundException("Restaurant not found");

    public Page<@NonNull Restaurant> getRestaurantsPage(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    public Restaurant getRestaurantById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> RESTAURANT_NOT_FOUND_EXCEPTION);
    }


    public Restaurant update(Restaurant newRestaurant) {
        final var id = newRestaurant.getId();
        final var restaurant = repository.findById(id)
                .orElseThrow(() -> RESTAURANT_NOT_FOUND_EXCEPTION);
        restaurant.setName(newRestaurant.getName());
        restaurant.setRating(newRestaurant.getRating());
        restaurant.setLocation(newRestaurant.getLocation());
        return repository.save(restaurant);
    }

    public Restaurant delete(UUID id) {
        final var restaurant = repository.findById(id)
                .orElseThrow(() -> RESTAURANT_NOT_FOUND_EXCEPTION);
        repository.deleteById(id);
        return restaurant;
    }

    public Restaurant getRestaurantByName(String name) {
        return repository.findByName(name)
            .orElseThrow(() -> RESTAURANT_NOT_FOUND_EXCEPTION);
    }

    public List<Restaurant> getRestaurantByLocation(String location) {
        return repository.findByLocation(location);
    }

    public Restaurant create(RestaurantRequest restaurantRequest) {
        if(repository.existsByName(restaurantRequest.getName()))
            throw new EntityExistsException("Restaurant name already exists");
        final var restaurant = new Restaurant();
        restaurant.setLocation(restaurantRequest.getLocation());
        restaurant.setName(restaurantRequest.getName());
        restaurant.setRating(restaurantRequest.getRating());
        restaurant.setCreatedAt(Instant.now());
        return repository.save(restaurant);
    }
}

