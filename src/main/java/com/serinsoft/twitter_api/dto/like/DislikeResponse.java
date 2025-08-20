package com.serinsoft.twitter_api.dto.like;

import java.util.UUID;

public record DislikeResponse(
        UUID tweetId,
        long dislikes
) {
}
