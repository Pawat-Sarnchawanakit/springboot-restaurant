package sar.pawat.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "Username is required")
    @Size(min=4, message = "Username is mandatory and at least 4 characters in length")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min=8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Name must only contain letters")
    private String name;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
