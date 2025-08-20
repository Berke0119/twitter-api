package com.serinsoft.twitter_api.controller;

import com.serinsoft.twitter_api.dto.like.DislikeRequest;
import com.serinsoft.twitter_api.dto.like.DislikeResponse;
import com.serinsoft.twitter_api.dto.like.LikeRequest;
import com.serinsoft.twitter_api.dto.like.LikeResponse;
import com.serinsoft.twitter_api.security.PrincipalUser;
import com.serinsoft.twitter_api.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<LikeResponse> like(Authentication auth, @RequestBody LikeRequest req) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        return ResponseEntity.ok(likeService.like(p.id(), req));
    }


    @PostMapping("/dislike")
    public ResponseEntity<DislikeResponse> dislike(Authentication auth, @RequestBody DislikeRequest req) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        return ResponseEntity.ok(likeService.dislike(p.id(), req));
    }
}
