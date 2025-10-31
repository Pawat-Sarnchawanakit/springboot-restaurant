package sar.pawat.restaurant.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sar.pawat.restaurant.entity.User;


import java.util.UUID;


@Repository
public interface UserRepository
        extends JpaRepository<@NonNull User, @NonNull UUID> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
