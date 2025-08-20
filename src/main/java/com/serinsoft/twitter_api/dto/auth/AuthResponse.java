package com.serinsoft.twitter_api.dto.auth;

import com.serinsoft.twitter_api.dto.common.UserSummary;

public record AuthResponse(
        String accessToken,
        String tokenType,
        UserSummary userSummary
) {
}
