package sar.pawat.restaurant.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import sar.pawat.restaurant.repository.UserRepository;
import sar.pawat.restaurant.service.CustomUserDetailsService;
import sar.pawat.restaurant.service.UserService;

import java.time.Instant;

@Component
public class AuthenticationEventListener {
    Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public AuthenticationEventListener(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        final var user = (User)event.getAuthentication().getPrincipal();
        final var userDetails = userRepository.findByUsername(user.getUsername());
        logger.info("{} [{}] has successfully logged in at {}", user.getUsername(), userDetails.getRole(), Instant.now());
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        final var username = (String) event.getAuthentication().getPrincipal();
        logger.warn("Failed login attempt [incorrect {}] at {}", userService.userExists(username) ? "PASSWORD" : "USERNAME", Instant.now());
    }
}
