package com.serinsoft.twitter_api.dto.tweet;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record UpdateTweetRequest(
        @Nullable @Size(max = 280) String content
) {
}
