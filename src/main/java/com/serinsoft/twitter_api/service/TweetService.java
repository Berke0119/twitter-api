package com.serinsoft.twitter_api.service;

import com.serinsoft.twitter_api.dto.common.UserSummary;
import com.serinsoft.twitter_api.dto.mapper.TweetMapper;
import com.serinsoft.twitter_api.dto.mapper.UserMapper;
import com.serinsoft.twitter_api.dto.tweet.CreateTweetRequest;
import com.serinsoft.twitter_api.dto.tweet.TweetResponse;
import com.serinsoft.twitter_api.dto.tweet.UpdateTweetRequest;
import com.serinsoft.twitter_api.entity.Tweet;
import com.serinsoft.twitter_api.entity.User;
import com.serinsoft.twitter_api.exception.AccessDeniedBusinessException;
import com.serinsoft.twitter_api.exception.NotFoundException;
import com.serinsoft.twitter_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.Cache;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final TweetLikeRepository likeRepository;
    private final RetweetRepository retweetRepository;
    private final CommentRepository commentRepository;
    private final CacheManager cacheManager;


    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "TWEET_PAGE_BY_USER", allEntries = true)
    })
    public TweetResponse create(UUID userId, CreateTweetRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        Tweet t = new Tweet();
        t.setUser(user);
        t.setContent(req.content());
        tweetRepository.save(t);


        evictTweetById(t.getId());

        return buildTweetResponse(t, user, userId);
    }


    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "TWEET_BY_ID", key = "'tweet:' + #tweetId")
    public TweetResponse findById(UUID tweetId, UUID requesterId) {
        Tweet t = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new NotFoundException("TWEET_NOT_FOUND"));

        User author = t.getUser();
        return buildTweetResponse(t, author, requesterId);
    }

    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "TWEET_PAGE_BY_USER",
            key = "'tweet:user:' + #userId + ':page=' + #pageable.pageNumber + ':size=' + #pageable.pageSize"
    )
    public Page<TweetResponse> findByUser(UUID userId, Pageable pageable, UUID requesterId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));
        UserSummary authorSummary = UserMapper.toSummary(author);

        return tweetRepository.findByUser_Id(userId, pageable)
                .map(t -> {
                    long likes = likeCount(t.getId());
                    long rts = retweetCount(t.getId());
                    long cmts = commentCount(t.getId());
                    boolean likedByMe = likeRepository.existsByTweet_IdAndUser_Id(t.getId(), requesterId);
                    boolean retweetedByMe = retweetRepository.existsByTweet_IdAndUser_Id(t.getId(), requesterId);
                    return TweetMapper.toResponse(t, authorSummary, likes, rts, cmts, likedByMe, retweetedByMe);
                });
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "TWEET_BY_ID", key = "'tweet:' + #tweetId"),
            @CacheEvict(cacheNames = "TWEET_PAGE_BY_USER", allEntries = true)
    })
    public TweetResponse update(UUID tweetId, UUID requesterId, UpdateTweetRequest req) {
        Tweet t = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new NotFoundException("TWEET_NOT_FOUND"));

        if (!t.getUser().getId().equals(requesterId)) {
            throw new AccessDeniedBusinessException("ONLY_OWNER");
        }

        if (req.content() != null) {
            t.setContent(req.content());
        }
        tweetRepository.save(t);

        return buildTweetResponse(t, t.getUser(), requesterId);
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "TWEET_BY_ID", key = "'tweet:' + #tweetId"),
            @CacheEvict(cacheNames = "TWEET_PAGE_BY_USER", allEntries = true),
            @CacheEvict(cacheNames = "LIKE_COUNT", key = "'likeCount:tweet:' + #tweetId"),
            @CacheEvict(cacheNames = "RETWEET_COUNT", key = "'retweetCount:tweet:' + #tweetId"),
            @CacheEvict(cacheNames = "COMMENT_COUNT", key = "'commentCount:tweet:' + #tweetId")
    })
    public void delete(UUID tweetId, UUID requesterId) {
        Tweet t = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new NotFoundException("TWEET_NOT_FOUND"));
        if (!t.getUser().getId().equals(requesterId)) {
            throw new AccessDeniedBusinessException("ONLY_OWNER");
        }
        tweetRepository.delete(t);
    }

    @Cacheable(cacheNames = "LIKE_COUNT", key = "'likeCount:tweet:' + #tweetId")
    public long likeCount(UUID tweetId) {
        return likeRepository.countByTweet_Id(tweetId);
    }

    @Cacheable(cacheNames = "RETWEET_COUNT", key = "'retweetCount:tweet:' + #tweetId")
    public long retweetCount(UUID tweetId) {
        return retweetRepository.countByTweet_Id(tweetId);
    }

    @Cacheable(cacheNames = "COMMENT_COUNT", key = "'commentCount:tweet:' + #tweetId")
    public long commentCount(UUID tweetId) {
        return commentRepository.countByTweet_Id(tweetId);
    }



    /*  HELPERS */
    private void evictTweetById(UUID id) {
        Cache c = cacheManager.getCache("TWEET_BY_ID");
        if (c != null) c.evict("tweet:" + id);
    }


    private TweetResponse buildTweetResponse(Tweet t, User author, UUID requesterId) {
        UserSummary authorSummary = UserMapper.toSummary(author);
        long likes = likeCount(t.getId());
        long rts = retweetCount(t.getId());
        long cmts = commentCount(t.getId());
        boolean likedByMe = likeRepository.existsByTweet_IdAndUser_Id(t.getId(), requesterId);
        boolean retweetedByMe = retweetRepository.existsByTweet_IdAndUser_Id(t.getId(), requesterId);
        return TweetMapper.toResponse(t, authorSummary, likes, rts, cmts, likedByMe, retweetedByMe);
    }

}
