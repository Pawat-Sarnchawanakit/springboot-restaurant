package sar.pawat.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sar.pawat.restaurant.entity.Restaurant;
import sar.pawat.restaurant.repository.RestaurantRepository;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public Optional<Restaurant> getRestaurantById(UUID id) {
        return repository.findById(id);
    }


    public Restaurant update(Restaurant newRestaurant) {
        final var id = newRestaurant.getId();
        final var recordOptional = repository.findById(id);
        if(recordOptional.isEmpty())
            return null;
        final var record = recordOptional.get();
        record.setName(newRestaurant.getName());
        record.setRating(newRestaurant.getRating());
        record.setLocation(newRestaurant.getLocation());
        return repository.save(record);
    }

    public Restaurant delete(UUID id) {
        final var recordOptional = repository.findById(id);
        if(recordOptional.isEmpty())
            return null;
        repository.deleteById(id);
        return recordOptional.get();
    }

    public Restaurant getRestaurantByName(String name) {
        return repository.findByName(name);
    }

    public List<Restaurant> getRestaurantByLocation(String location) {
        return repository.findByLocation(location);
    }

    public Restaurant create(Restaurant restaurant) {
        restaurant.setCreatedAt(Instant.now());
        return repository.save(restaurant);
    }
}

