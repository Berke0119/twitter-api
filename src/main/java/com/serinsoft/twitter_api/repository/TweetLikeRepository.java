package com.serinsoft.twitter_api.repository;

import com.serinsoft.twitter_api.entity.TweetLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TweetLikeRepository extends JpaRepository<TweetLike, UUID> {

    Optional<TweetLike> findByTweet_IdAndUser_Id(UUID tweetId, UUID userId);

    boolean existByTweet_IdAndUser_Id(UUID tweetId, UUID userId);

    long countByTweet_Id(UUID tweetId);

    long deleteByTweet_IdAndUser_Id(UUID tweetId, UUID userId);

}
