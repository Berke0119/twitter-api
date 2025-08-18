package com.serinsoft.twitter_api.repository;

import com.serinsoft.twitter_api.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RetweetRepository extends JpaRepository<Retweet, UUID> {

    Optional<Retweet> findByTweet_IdAndUser_Id(UUID tweetId, UUID userId);

    boolean existsByTweet_IdAndUser_Id(UUID tweetId, UUID userId);

    boolean existsByIdAndUser_Id(UUID id, UUID userId);

    long countByTweet_Id(UUID tweetId);

    long deleteByIdAndUser_Id(UUID id, UUID userId);

    long deleteByTweet_IdAndUser_Id(UUID tweetId, UUID userId);

}
