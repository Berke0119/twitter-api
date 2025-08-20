package com.serinsoft.twitter_api.dto.mapper;

import com.serinsoft.twitter_api.dto.common.UserSummary;
import com.serinsoft.twitter_api.dto.tweet.TweetResponse;
import com.serinsoft.twitter_api.entity.Tweet;

public final class TweetMapper {

    private TweetMapper() {

    }

    public static TweetResponse toResponse(
            Tweet t,
            UserSummary author,
            long likes,
            long retweets,
            long comments,
            boolean likedByRequester,
            boolean retweetedByRequester
    ){
        return new TweetResponse(
                t.getId(),
                t.getContent(),
                author,
                t.getCreatedAt(),
                t.getUpdatedAt(),
                new TweetResponse.Stats(likes, retweets, comments),
                likedByRequester,
                retweetedByRequester
        );
    }

}
