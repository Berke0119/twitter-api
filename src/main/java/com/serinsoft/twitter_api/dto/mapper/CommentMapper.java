package com.serinsoft.twitter_api.dto.mapper;

import com.serinsoft.twitter_api.dto.comment.CommentResponse;
import com.serinsoft.twitter_api.dto.common.UserSummary;
import com.serinsoft.twitter_api.entity.Comment;

public class CommentMapper {

    private CommentMapper() {}

    public static CommentResponse toResponse(Comment c, UserSummary author) {
        return new CommentResponse(
                c.getId(),
                c.getTweet().getId(),
                c.getContent(),
                author,
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
