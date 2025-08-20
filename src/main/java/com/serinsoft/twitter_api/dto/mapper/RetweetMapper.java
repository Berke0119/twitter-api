package com.serinsoft.twitter_api.dto.mapper;

import com.serinsoft.twitter_api.dto.retweet.RetweetResponse;
import com.serinsoft.twitter_api.entity.Retweet;

public class RetweetMapper {

    private RetweetMapper() {}

    public static RetweetResponse toResponse(Retweet r, long retweetCount) {
        return new RetweetResponse(
                r.getId(),
                r.getTweet().getId(),
                r.getUser().getId(),
                retweetCount,
                r.getCreatedAt()
        );
    }
}
