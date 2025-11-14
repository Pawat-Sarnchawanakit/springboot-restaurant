package sar.pawat.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;
import sar.pawat.restaurant.security.AttributeEncryptor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String username;
    private String password;

    @Convert(converter = AttributeEncryptor.class)
    private String name;
    private String role;
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
