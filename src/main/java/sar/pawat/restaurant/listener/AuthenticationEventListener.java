package sar.pawat.restaurant.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import sar.pawat.restaurant.service.UserService;

import java.time.Instant;

@Component
public class AuthenticationEventListener {
    Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);
    private UserService userService;

    @Autowired
    public AuthenticationEventListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        final var user = (User)event.getAuthentication().getPrincipal();
        logger.info("{} has successfully logged in at {}", user.getUsername(), Instant.now());
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        final var username = (String) event.getAuthentication().getPrincipal();
        logger.warn("Failed login attempt [incorrect {}] at {}", userService.userExists(username) ? "PASSWORD" : "USERNAME", Instant.now());
    }
}
