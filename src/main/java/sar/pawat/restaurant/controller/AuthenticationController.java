package sar.pawat.restaurant.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sar.pawat.restaurant.dto.GoogleAuthRequest;
import sar.pawat.restaurant.dto.LoginRequest;
import sar.pawat.restaurant.dto.SignupRequest;
import sar.pawat.restaurant.dto.UserInfoResponse;
import sar.pawat.restaurant.entity.User;
import sar.pawat.restaurant.security.JwtUtil;
import sar.pawat.restaurant.service.UserService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Value("${google.clientId}")
    private String googleClientId;
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

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleAuthRequest request) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        ).setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(request.credential);

        if (idToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

        String email = idToken.getPayload().getEmail();
        String name = (String) idToken.getPayload().get("name");

        User user = userService.findOrCreateGoogleUser(email, name);
        String token = jwtUtils.generateToken(user.getUsername());

        // Create session cookie
        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60 * 60)         // 1 hour
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                        "message", "Successfully logged in using Google"
                ));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractTokenFromCookie(request);
        if (token != null)
            jwtUtils.invalidateToken(token);

        // Clear cookie
        ResponseCookie cleared = ResponseCookie.from(AUTH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)       // expires immediately
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cleared.toString());
        return ResponseEntity.ok("Logged out");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        if (token == null)
            return ResponseEntity.status(401).body("No auth token");
        String username = jwtUtils.getUsernameFromToken(token);
        if (username == null)
            return ResponseEntity.status(401).body("Invalid token");
        final var user = userService.getUser(username);
        if (user == null)
            return ResponseEntity.status(404).body("User not found");
        return ResponseEntity.ok(new UserInfoResponse(username, user.getRole()));
    }


    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies())
            if (AUTH_COOKIE_NAME.equals(cookie.getName()))
                return cookie.getValue();
        return null;
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
