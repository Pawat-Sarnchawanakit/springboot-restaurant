package sar.pawat.restaurant.service;


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

    public void createUser(SignupRequest request) {
        User dao = new User();
        dao.setUsername(request.getUsername());
        dao.setPassword(encoder.encode(request.getPassword()));
        dao.setName(request.getName());
        dao.setRole("ROLE_USER");
        dao.setCreatedAt(Instant.now());
        userRepository.save(dao);
    }
}
