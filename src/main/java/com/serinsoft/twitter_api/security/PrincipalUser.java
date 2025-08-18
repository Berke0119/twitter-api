package com.serinsoft.twitter_api.security;

import java.util.UUID;

public record PrincipalUser(UUID id, String username) {
}
