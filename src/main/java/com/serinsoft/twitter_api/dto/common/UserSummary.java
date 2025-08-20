package com.serinsoft.twitter_api.dto.common;

import java.util.UUID;

public record UserSummary(
        UUID id,
        String username,
        String displayName,
        String avatarUrl
) {
}
