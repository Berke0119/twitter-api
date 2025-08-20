package com.serinsoft.twitter_api.dto.tweet;

import com.serinsoft.twitter_api.dto.common.UserSummary;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TweetResponse(
        UUID id,
        String content,
        UserSummary author,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Stats stats,
        boolean likedByRequester,
        boolean retweetedByRequester
) {

    public record Stats(
            long likes,
            long retweets,
            long comments
    ){


    }

}
