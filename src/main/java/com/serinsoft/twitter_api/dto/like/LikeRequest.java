package com.serinsoft.twitter_api.dto.like;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LikeRequest(
        @NotNull UUID tweetId
) {
}
