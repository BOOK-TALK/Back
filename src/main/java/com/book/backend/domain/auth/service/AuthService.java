package com.book.backend.domain.auth.service;

import com.book.backend.domain.auth.dto.JwtTokenDto;
import com.book.backend.domain.auth.dto.LoginDto;
import com.book.backend.domain.auth.dto.LoginSuccessResponseDto;
import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.auth.entity.RefreshToken;
import com.book.backend.domain.auth.mapper.AuthMapper;
import com.book.backend.domain.auth.repository.RefreshTokenRepository;
import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserDto signup(SignupDto signupDto) {
        log.trace("signup()");

        validateNotDuplicatedLoginId(signupDto.getLoginId());

        User user = authMapper.convertToUser(signupDto);
        user.setRegDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return userMapper.convertToUserDto(savedUser);
    }

    public LoginSuccessResponseDto login(LoginDto loginDto) {
        log.trace("login()");

        try {
            // 사용자 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword()));

            // 인증 성공 시 Security Context에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 인증 성공 후 유저 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getLoginId());
        JwtTokenDto jwtTokenDto = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByLoginId(loginDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        jwtService.updateRefreshToken(jwtTokenDto, user);

        return LoginSuccessResponseDto.builder()
                .userId(user.getUserId())
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .build();
    }

    @Transactional
    public void deleteAccountByLoginId(String loginId) {
        log.trace("deleteAccountByLoginId()");

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    private void validateNotDuplicatedLoginId(String loginId) {
        log.trace("validateNotDuplicatedByLoginId()");

        Optional<User> userOptional = userRepository.findByLoginId(loginId);

        if (userOptional.isPresent()) {
            throw new CustomException(ErrorCode.LOGIN_ID_DUPLICATED);
        }
    }
}
