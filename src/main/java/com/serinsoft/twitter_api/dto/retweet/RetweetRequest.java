package com.serinsoft.twitter_api.dto.retweet;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RetweetRequest(
        @NotNull UUID tweetId
) {
}
