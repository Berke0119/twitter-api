package com.serinsoft.twitter_api.controller;


import com.serinsoft.twitter_api.dto.tweet.CreateTweetRequest;
import com.serinsoft.twitter_api.dto.tweet.TweetResponse;
import com.serinsoft.twitter_api.dto.tweet.UpdateTweetRequest;
import com.serinsoft.twitter_api.security.PrincipalUser;
import com.serinsoft.twitter_api.service.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class TweetController {

    private final TweetService tweetService;


    @PostMapping("/tweet")
    public ResponseEntity<TweetResponse> create(Authentication auth, @RequestBody CreateTweetRequest req) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        TweetResponse resp = tweetService.create(p.id(), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    @GetMapping("/tweet/findById")
    public ResponseEntity<TweetResponse> findById(Authentication auth, @RequestParam UUID id) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        return ResponseEntity.ok(tweetService.findById(id, p.id()));
    }


    @GetMapping("/tweet/findByUserId")
    public ResponseEntity<Page<TweetResponse>> findByUserId(
            Authentication auth,
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(tweetService.findByUser(userId, pageable, p.id()));
    }


    @PutMapping("/tweet/{id}")
    public ResponseEntity<TweetResponse> update(
            Authentication auth,
            @PathVariable UUID id,
            @RequestBody UpdateTweetRequest req
    ) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        return ResponseEntity.ok(tweetService.update(id, p.id(), req));
    }


    @DeleteMapping("/tweet/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable UUID id) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        tweetService.delete(id, p.id());
        return ResponseEntity.noContent().build();
    }
}
