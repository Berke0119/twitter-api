package com.serinsoft.twitter_api.repository;

import com.serinsoft.twitter_api.entity.Tweet;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface TweetRepository extends JpaRepository<Tweet, UUID> {

   Page<Tweet> findByUser_Id(UUID userId, Pageable pageable);

   boolean existsByIdAndUser_Id(UUID tweetId, UUID userId);

   long countByUser_Id(UUID userId);

   Optional<Tweet> findById(UUID id);

   long deleteByIdAndUser_Id(UUID tweetId, UUID userId);

}
