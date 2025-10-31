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

    public Page<@NonNull Restaurant> getRestaurantsPage(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    public Restaurant getRestaurantById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
    }


    public Restaurant update(Restaurant newRestaurant) {
        final var id = newRestaurant.getId();
        final var record = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        record.setName(newRestaurant.getName());
        record.setRating(newRestaurant.getRating());
        record.setLocation(newRestaurant.getLocation());
        return repository.save(record);
    }

    public Restaurant delete(UUID id) {
        final var record = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        repository.deleteById(id);
        return record;
    }

    public Restaurant getRestaurantByName(String name) {
        return repository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
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

