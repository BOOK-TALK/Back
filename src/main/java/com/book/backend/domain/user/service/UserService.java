package com.book.backend.domain.user.service;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.user.dto.LibraryDto;
import com.book.backend.domain.user.dto.UserInfoDto;
import com.book.backend.domain.user.dto.UserLibrariesRequestDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.mapper.UserMapper;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RequestValidate requestValidate;

    public User loadLoggedinUser() {
        log.trace("UserService > loadLoggedinUser()");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return findByUsername(username);
    }

    // username으로 사용자 조회
    public User findByUsername(String username) {
        log.trace("UserService > findByUsername()");

        // TODO: 리팩토링 필요
        try {
            return findByLoginId(username);
        } catch (IllegalArgumentException e1) {
            try {
                return findByKakaoId(username);
            } catch (IllegalArgumentException e2) {
                try {
                    return findByAppleId(username);
                } catch (IllegalArgumentException e3) {
                    return null;
                }
            }
        }
    }

    private User findByLoginId(String loginId) {
        log.trace("UserService > findByLoginId()");
        return userRepository.findByLoginId(loginId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private User findByKakaoId(String kakaoId) {
        log.trace("UserService > findByKakaoId()");
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private User findByAppleId(String appleId) {
        log.trace("UserService > findByAppleId()");
        return userRepository.findByAppleId(appleId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public User updateUserInfo(User user, UserInfoDto requestDto) {
        log.trace("UserService > updateUserInfo()");

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        user.setNickname(requestDto.getNickname());
        user.setGender(userMapper.convertStringToGender(requestDto.getGender()));
        user.setBirthDate(requestDto.getBirthDate());
        userRepository.save(user);

        return user;
    }

    @Transactional
    public User updateUserLibraries(User user, UserLibrariesRequestDto dto) {
        log.trace("UserService > updateUserLibraries()");

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 리스트 사이즈가 3보다 크면 오류 반환
        if (dto.getLibraries().size() > 3) {
            throw new CustomException(ErrorCode.LIST_SIZE_EXCEEDED);
        }

        user.getLibraries().clear();

        for (LibraryDto library : dto.getLibraries()) {
            if (library.getCode().isBlank() || library.getName().isBlank()) {
                continue;
            }

            requestValidate.isValidLibCode(library.getCode());
            user.getLibraries().add(library);
        }
        userRepository.save(user);

        return user;
    }

    public List<LibraryDto> getLibraries(User user) {
        log.trace("UserService > getLibraries()");

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return user.getLibraries();
    }
}
