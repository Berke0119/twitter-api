package com.serinsoft.twitter_api.service;

import com.serinsoft.twitter_api.dto.like.DislikeRequest;
import com.serinsoft.twitter_api.dto.like.DislikeResponse;
import com.serinsoft.twitter_api.dto.like.LikeRequest;
import com.serinsoft.twitter_api.dto.like.LikeResponse;
import com.serinsoft.twitter_api.dto.mapper.ReactionMapper;
import com.serinsoft.twitter_api.entity.Tweet;
import com.serinsoft.twitter_api.entity.TweetLike;
import com.serinsoft.twitter_api.entity.User;
import com.serinsoft.twitter_api.exception.NotFoundException;
import com.serinsoft.twitter_api.repository.TweetLikeRepository;
import com.serinsoft.twitter_api.repository.TweetRepository;
import com.serinsoft.twitter_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final TweetLikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Transactional
    public LikeResponse like(UUID requesterId, LikeRequest req) {
        UUID tweetId = req.tweetId();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new NotFoundException("TWEET_NOT_FOUND"));

        User user = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));


        likeRepository.findByTweet_IdAndUser_Id(tweetId, requesterId).orElseGet(() -> {
            TweetLike like = new TweetLike();
            like.setTweet(tweet);
            like.setUser(user);
            return likeRepository.save(like);
        });


        evictAfterLikeChange(tweetId);

        long newCount = likeRepository.countByTweet_Id(tweetId);
        return ReactionMapper.likeResponse(tweetId, newCount);
    }

    @Transactional
    public DislikeResponse dislike(UUID requesterId, DislikeRequest req) {
        UUID tweetId = req.tweetId();

        tweetRepository.findById(tweetId)
                .orElseThrow(() -> new NotFoundException("TWEET_NOT_FOUND"));

        likeRepository.deleteByTweet_IdAndUser_Id(tweetId, requesterId);


        evictAfterLikeChange(tweetId);

        long newCount = likeRepository.countByTweet_Id(tweetId);
        return ReactionMapper.dislikeResponse(tweetId, newCount);
    }



    private void evictAfterLikeChange(UUID tweetId) {
        Cache likeCount = cacheManager.getCache("LIKE_COUNT");
        if (likeCount != null)  likeCount.evict("likeCount:tweet:" + tweetId);

        Cache tweetById = cacheManager.getCache("TWEET_BY_ID");
        if (tweetById != null) tweetById.evict("tweet:" + tweetId);

        Cache pages = cacheManager.getCache("TWEET_PAGE_BY_USER");
        if (pages != null) pages.clear();
    }

}
