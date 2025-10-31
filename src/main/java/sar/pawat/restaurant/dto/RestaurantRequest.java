package sar.pawat.restaurant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not be greater than 5")
    private double rating;

    @NotBlank(message = "Location is mandatory")
    private String location;

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getLocation() {
        return location;
    }
}
