package com.serinsoft.twitter_api.dto.retweet;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RetweetResponse(
        UUID id,
        UUID tweetId,
        UUID userId,
        long retweets,
        OffsetDateTime createdAt

) {
}
