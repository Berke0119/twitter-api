package com.serinsoft.twitter_api.dto.mapper;

import com.serinsoft.twitter_api.dto.like.DislikeResponse;
import com.serinsoft.twitter_api.dto.like.LikeResponse;

import java.util.UUID;

public class ReactionMapper {

    private ReactionMapper() {}

    public static LikeResponse likeResponse(UUID tweetId, long likes) {
        return new LikeResponse(tweetId, likes);
    }

    public static DislikeResponse dislikeResponse(UUID tweetId, long likes) {
        return new DislikeResponse(tweetId, likes);
    }
}
