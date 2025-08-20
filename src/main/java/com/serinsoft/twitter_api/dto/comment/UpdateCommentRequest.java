package com.serinsoft.twitter_api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommentRequest(
        @NotBlank @Size String content
) {
}
