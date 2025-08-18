package com.serinsoft.twitter_api.exception;

public record ApiFieldError(String field, String message) {
    public static ApiFieldError of(String field, String message) {
        return new ApiFieldError(field, message);
    }
}
