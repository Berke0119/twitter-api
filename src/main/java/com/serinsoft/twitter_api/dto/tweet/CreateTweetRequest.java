package com.serinsoft.twitter_api.dto.tweet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTweetRequest(
        @NotBlank @Size(max = 280) String content
) {
}
