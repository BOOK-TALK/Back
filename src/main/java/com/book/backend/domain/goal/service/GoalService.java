package com.book.backend.domain.goal.service;

import com.book.backend.domain.book.dto.BookInfoDto;
import com.book.backend.domain.book.dto.BookSummaryDto;
import com.book.backend.domain.book.mapper.BookMapper;
import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.goal.mapper.GoalMapper;
import com.book.backend.domain.goal.repository.GoalRepository;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserService userService;
    private final GoalMapper goalMapper;

    @Transactional
    public GoalDto createGoal(String isbn, String totalPage) throws Exception {
        User user = validateAndGetLoggedInUser();

        // TODO: 이미 해당 책에 대한 목표가 생성되어 있는 경우 예외처리

        LocalDateTime now = LocalDateTime.now();

        Goal goal = Goal.builder()
                .isbn(isbn)
                .user(user)
                .recentPage(null)
                .totalPage(totalPage)
                .createdAt(now)
                .updatedAt(now)
                .isFinished(false)
                .build();

        goalRepository.save(goal);

        return goalMapper.convertToGoalDto(goal);
    }

    @Transactional
    public GoalDto finishGoal(String goalId) throws Exception {
        User user = validateAndGetLoggedInUser();
        Goal goal = validateAndGetGoal(goalId);
        validateUserMatchesGoal(user, goal);

        goal.setIsFinished(true);
        goalRepository.save(goal);

        return goalMapper.convertToGoalDto(goal);
    }

    // 유저 검증 및 로그인된 유저 로드
    private User validateAndGetLoggedInUser() {
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    // 목표 검증 및 로드
    private Goal validateAndGetGoal(String goalId) {
        return goalRepository.findById(Long.valueOf(goalId))
                .orElseThrow(() -> new CustomException(ErrorCode.GOAL_NOT_FOUND));
    }

    // 해당 유저의 목표인지 검증
    private void validateUserMatchesGoal(User user, Goal goal) {
        if (user != goal.getUser()) {
            throw new CustomException(ErrorCode.CANNOT_ACCESS_GOAL);
        }
    }
}