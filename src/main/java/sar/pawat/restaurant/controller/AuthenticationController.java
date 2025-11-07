package sar.pawat.restaurant.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sar.pawat.restaurant.dto.LoginRequest;
import sar.pawat.restaurant.dto.SignupRequest;
import sar.pawat.restaurant.security.JwtUtil;
import sar.pawat.restaurant.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    @Autowired
    public AuthenticationController(final UserService userService, final AuthenticationManager authenticationManager, final JwtUtil jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/login")
    public ResponseEntity<@NonNull String> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        if(userDetails == null)
            return ResponseEntity.badRequest().body("Invalid user");
        return ResponseEntity.ok(jwtUtils.generateToken(userDetails.getUsername()));
    }


    @PostMapping("/signup")
    public ResponseEntity<@NonNull String> registerUser(@Valid @RequestBody SignupRequest request) {
        userService.createUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }
}
