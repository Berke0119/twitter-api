package com.serinsoft.twitter_api.dto.comment;

import com.serinsoft.twitter_api.dto.common.UserSummary;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        UUID tweetId,
        String content,
        UserSummary author,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
