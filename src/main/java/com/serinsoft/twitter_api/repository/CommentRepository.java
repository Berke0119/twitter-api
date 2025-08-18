package com.serinsoft.twitter_api.repository;

import com.serinsoft.twitter_api.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByTweet_IdOrderByCreatedAtAsc(UUID tweetId);

    Page<Comment> findByTweet_Id(UUID tweetId, Pageable pageable);

    long countByTweet_Id(UUID tweetId);

    Optional<Comment> findById(UUID id);

    boolean existsByIdAndUser_Id(UUID commentId, UUID userId);

    long deleteByIdAndUser_Id(UUID commentId, UUID userId);
}
