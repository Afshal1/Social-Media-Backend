package com.example.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleResponse {

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private String emailVerified;

    @JsonProperty("access_type")
    private String accessType;

    @JsonProperty("username")
    private String username;

    @JsonProperty("isUserExists")
    private boolean isUserExists;
}
