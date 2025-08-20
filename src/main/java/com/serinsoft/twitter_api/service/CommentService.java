package com.serinsoft.twitter_api.service;

import com.serinsoft.twitter_api.dto.comment.CommentResponse;
import com.serinsoft.twitter_api.dto.comment.CreateCommentRequest;
import com.serinsoft.twitter_api.dto.comment.UpdateCommentRequest;
import com.serinsoft.twitter_api.dto.common.UserSummary;
import com.serinsoft.twitter_api.dto.mapper.CommentMapper;
import com.serinsoft.twitter_api.dto.mapper.UserMapper;
import com.serinsoft.twitter_api.entity.Comment;
import com.serinsoft.twitter_api.entity.Tweet;
import com.serinsoft.twitter_api.entity.User;
import com.serinsoft.twitter_api.exception.AccessDeniedBusinessException;
import com.serinsoft.twitter_api.exception.NotFoundException;
import com.serinsoft.twitter_api.repository.CommentRepository;
import com.serinsoft.twitter_api.repository.TweetRepository;
import com.serinsoft.twitter_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Transactional
    public CommentResponse create(UUID requesterId, CreateCommentRequest req){

        Tweet tweet = tweetRepository.findById(req.tweetId())
                .orElseThrow(() -> new NotFoundException("TWEET_NOT_FOUND"));
        User user = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        Comment comment = new Comment();
        comment.setTweet(tweet);
        comment.setUser(user);
        comment.setContent(req.content());
        commentRepository.save(comment);

        evictAfterChange(tweet.getId());

        UserSummary author = UserMapper.toSummary(user);
        return CommentMapper.toResponse(comment, author);

    }

    @Transactional
    public CommentResponse update(UUID commentId, UUID requesterId, UpdateCommentRequest req) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));

        if (!comment.getUser().getId().equals(requesterId)) {
            throw new AccessDeniedBusinessException("ONLY_COMMENT_OWNER");
        }

        comment.setContent(req.content());
        commentRepository.save(comment);


        evictTweetAndPages(comment.getTweet().getId());

        UserSummary author = UserMapper.toSummary(comment.getUser());
        return CommentMapper.toResponse(comment, author);
    }

    @Transactional
    public void delete(UUID commentId, UUID requesterId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));

        UUID ownerId = comment.getUser().getId();
        UUID tweetOwnerId = comment.getTweet().getUser().getId();

        if (!requesterId.equals(ownerId) && !requesterId.equals(tweetOwnerId)) {
            throw new AccessDeniedBusinessException("ONLY_COMMENT_OR_TWEET_OWNER");
        }

        UUID tweetId = comment.getTweet().getId();
        commentRepository.delete(comment);

        evictAfterChange(tweetId);
    }


    /* HELPERS */
    private void evictAfterChange(UUID tweetId) {
        evictCommentCount(tweetId);
        evictTweetAndPages(tweetId);
    }

    private void evictCommentCount(UUID tweetId) {
        Cache cc = cacheManager.getCache("COMMENT_COUNT");
        if (cc != null) cc.evict("commentCount:tweet:" + tweetId);
    }

    private void evictTweetAndPages(UUID tweetId) {
        Cache tbid = cacheManager.getCache("TWEET_BY_ID");
        if (tbid != null) tbid.evict("tweet:" + tweetId);

        Cache pages = cacheManager.getCache("TWEET_PAGE_BY_USER");
        if (pages != null) pages.clear();
    }

}
