package com.serinsoft.twitter_api.dto.mapper;

import com.serinsoft.twitter_api.dto.common.UserSummary;
import com.serinsoft.twitter_api.entity.User;

public final class UserMapper {

    private UserMapper(){

    }

    public static UserSummary toSummary(User user){
        if(user == null) {
            return null;
        }

        return new UserSummary(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getAvatarUrl()
        );
    }
}
