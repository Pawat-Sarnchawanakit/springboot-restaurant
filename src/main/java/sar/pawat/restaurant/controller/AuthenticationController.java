package sar.pawat.restaurant.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private static final String AUTH_COOKIE_NAME = "token";
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
    public ResponseEntity<@NonNull Map<String, String>> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        if(userDetails == null)
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user"));
        final var token = jwtUtils.generateToken(userDetails.getUsername());

        // Create HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
                .httpOnly(true)          // Javascript cannot read cookie
                .secure(true)            // HTTPS only
                .path("/")
                .maxAge(60 * 60)         // 1 hour
                .sameSite("Strict")
                .build();

        // Return cookie in response headers, optional JSON body
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                    "message", "Successfully logged in"
                    )
                );
    }

    @PostMapping("/signup")
    public ResponseEntity<@NonNull String> registerUser(@Valid @RequestBody SignupRequest request) {
        userService.createUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }
}
