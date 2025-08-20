package com.serinsoft.twitter_api.dto.like;

import java.util.UUID;

public record LikeResponse(
        UUID tweetId,
        long likes
) {
}
