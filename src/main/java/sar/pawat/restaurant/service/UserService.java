package sar.pawat.restaurant.service;


import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sar.pawat.restaurant.dto.SignupRequest;
import sar.pawat.restaurant.entity.User;
import sar.pawat.restaurant.repository.UserRepository;

import java.time.Instant;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void createUser(SignupRequest request) {
        if(userExists(request.getUsername()))
            throw new EntityExistsException("Error: Username is already taken!");
        User dao = new User();
        dao.setUsername(request.getUsername());
        dao.setPassword(encoder.encode(request.getPassword()));
        dao.setName(request.getName());
        dao.setRole("ROLE_USER");
        dao.setCreatedAt(Instant.now());
        userRepository.save(dao);
    }

    public User findOrCreateGoogleUser(String email, String name) {
        User user = userRepository.findByUsername(email);
        if (user == null) {
            User dao = new User();
            dao.setUsername(email);
            dao.setName(name);
            dao.setPassword(encoder.encode("NO_PASSWORD"));
            dao.setRole("ROLE_USER");
            dao.setCreatedAt(Instant.now());
            user = userRepository.save(dao);
        }
        return user;
    }

}
