package com.serinsoft.twitter_api.controller;

import com.serinsoft.twitter_api.dto.comment.CommentResponse;
import com.serinsoft.twitter_api.dto.comment.CreateCommentRequest;
import com.serinsoft.twitter_api.dto.comment.UpdateCommentRequest;
import com.serinsoft.twitter_api.security.PrincipalUser;
import com.serinsoft.twitter_api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> create(
            Authentication auth,
            @RequestBody CreateCommentRequest req
    ) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        CommentResponse response = commentService.create(p.id(), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/comment/{id}")
    public ResponseEntity<CommentResponse> update(
            Authentication auth,
            @PathVariable UUID id,
            @RequestBody UpdateCommentRequest req
    ) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        return ResponseEntity.ok(commentService.update(id, p.id(), req));
    }


    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable UUID id) {
        PrincipalUser p = (PrincipalUser) auth.getPrincipal();
        commentService.delete(id, p.id());
        return ResponseEntity.noContent().build();
    }

}
