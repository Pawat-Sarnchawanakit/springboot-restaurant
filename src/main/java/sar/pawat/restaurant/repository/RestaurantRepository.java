package sar.pawat.restaurant.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sar.pawat.restaurant.entity.Restaurant;


import java.util.List;
import java.util.UUID;


@Repository
public interface RestaurantRepository
        extends JpaRepository<@NonNull Restaurant, @NonNull UUID> {
    Restaurant findByName(String name);
    List<Restaurant> findByLocation(String location);
}
