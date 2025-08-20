package com.serinsoft.twitter_api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCommentRequest(
        @NotNull UUID tweetId,
        @NotBlank @Size(max = 280) String content
) {
}
