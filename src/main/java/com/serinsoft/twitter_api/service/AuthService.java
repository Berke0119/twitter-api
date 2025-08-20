package com.serinsoft.twitter_api.service;

import com.serinsoft.twitter_api.dto.auth.AuthResponse;
import com.serinsoft.twitter_api.dto.auth.LoginRequest;
import com.serinsoft.twitter_api.dto.auth.RegisterRequest;
import com.serinsoft.twitter_api.dto.common.UserSummary;
import com.serinsoft.twitter_api.dto.mapper.UserMapper;
import com.serinsoft.twitter_api.entity.User;
import com.serinsoft.twitter_api.exception.BusinessException;
import com.serinsoft.twitter_api.repository.UserRepository;
import com.serinsoft.twitter_api.security.JwtTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenService jwtTokenService;

    @Transactional
    public UserSummary register(RegisterRequest req){

        if(userRepository.existsByUsername(req.username())){
            throw new BusinessException("USERNAME_TAKEN");
        }

        if(userRepository.existsByEmail(req.email())){
            throw new BusinessException("EMAIL_TAKEN");
        }

        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setDisplayName(req.username());

        User saved = userRepository.save(user);
        return UserMapper.toSummary(saved);

    }


    @Transactional
    public AuthResponse login(LoginRequest req){

        User user = userRepository.findByUsername(req.usernameOrEmail())
                .or(() -> userRepository.findByEmail(req.usernameOrEmail()))
                .orElseThrow(() -> new BusinessException("INVALID_CREDENTIALS"));

        if(!passwordEncoder.matches(req.password(), user.getPasswordHash())){
            throw new BusinessException("INVALID_CREDENTIALS");
        }

        String token = jwtTokenService.generate(user.getId(), user.getUsername());
        return new AuthResponse(token, "Bearer", UserMapper.toSummary(user));


    }

}
