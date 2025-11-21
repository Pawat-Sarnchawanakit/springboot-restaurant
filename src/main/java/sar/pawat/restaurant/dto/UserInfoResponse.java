package sar.pawat.restaurant.dto;

import lombok.Getter;

@Getter
public class UserInfoResponse {
    private final String username;
    private final String role;

    public UserInfoResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
