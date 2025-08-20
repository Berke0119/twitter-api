package com.serinsoft.twitter_api.controller;

import com.serinsoft.twitter_api.dto.auth.AuthResponse;
import com.serinsoft.twitter_api.dto.auth.LoginRequest;
import com.serinsoft.twitter_api.dto.auth.RegisterRequest;
import com.serinsoft.twitter_api.dto.common.UserSummary;
import com.serinsoft.twitter_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserSummary> register(@RequestBody RegisterRequest req){
        UserSummary summaryUser = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(summaryUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(req));
    }

}
