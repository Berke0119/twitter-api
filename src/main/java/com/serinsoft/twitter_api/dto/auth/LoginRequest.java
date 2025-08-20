package com.serinsoft.twitter_api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String usernameOrEmail,
        @NotBlank String password
) {
}
