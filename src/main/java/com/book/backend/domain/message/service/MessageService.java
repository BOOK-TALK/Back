package com.book.backend.domain.message.service;

import com.book.backend.domain.auth.service.CustomUserDetailsService;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.goal.repository.GoalRepository;
import com.book.backend.domain.goal.service.GoalService;
import com.book.backend.domain.message.dto.MessageRequestDto;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.message.mapper.MessageMapper;
import com.book.backend.domain.message.repository.MessageRepository;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import com.book.backend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final OpentalkRepository opentalkRepository;
    private final GoalRepository goalRepository;
    private final MessageMapper messageMapper;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final RequestValidate requestValidate;
    private final UserService userService;

    public MessageResponseDto saveHttpMessage(Long opentalkId, String type, String content){
        log.trace("MessageService > saveHttpMessage()");
        if(type.equals("goal")){
            if(content == null) throw new CustomException(ErrorCode.INVALID_ISBN);
            requestValidate.isValidIsbn(content);
        }
        MessageRequestDto messageRequestDto = MessageRequestDto.builder()
                .opentalkId(opentalkId)
                .type(type)
                .build();
        if(content != null) messageRequestDto.setContent(content);

        Message message = messageMapper.convertToMessage(messageRequestDto);
        // message DB에 저장
        try{
            messageRepository.save(message);
        } catch (Exception e){
            throw new CustomException(ErrorCode.MESSAGE_SAVE_FAILED);
        }
        return messageMapper.convertToMessageResponseDto(message);
    }

    public MessageResponseDto shareGoal(Long opentalkId, String isbn){
        log.trace("MessageService > shareGoal()");
        User user = userService.loadLoggedinUser();
        Goal goal = goalRepository.findByUserAndIsbn(user, isbn).orElseThrow(() -> new CustomException(ErrorCode.GOAL_NOT_FOUND));

        MessageRequestDto messageRequestDto = MessageRequestDto.builder()
                .opentalkId(opentalkId)
                .type("goal")
                .content(String.valueOf(goal.getGoalId()))
                .build();
        Message message = messageMapper.convertToMessage(messageRequestDto);
        // message DB에 저장
        try{
            messageRepository.save(message);
        } catch (Exception e){
            throw new CustomException(ErrorCode.MESSAGE_SAVE_FAILED);
        }
        return messageMapper.convertToMessageResponseDto(message);
    }

    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto){
        log.trace("MessageService > saveMessage()");
        // 토큰 유효성 검사
        String token = messageRequestDto.getJwtToken();
        validateToken(token);

        // message DB에 저장
        Message message = messageMapper.convertToMessage(messageRequestDto);
        try{
            messageRepository.save(message);
        } catch (Exception e){
            throw new CustomException(ErrorCode.MESSAGE_SAVE_FAILED);
        }
        return messageMapper.convertToMessageResponseDto(message);
    }

    @Transactional
    public void validateToken(String token) {
        log.trace("MessageService > validateToken()");
        try{
            String username = jwtUtil.getUsernameFromToken(token);  // username 가져옴
            UserDetails userDetails;
            userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰 유효성 검증
            if (!jwtUtil.isValidToken(token, userDetails)) {
                throw new CustomException(ErrorCode.WRONG_JWT_TOKEN);
            } else {
                UsernamePasswordAuthenticationToken authenticated
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            }
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.WRONG_JWT_TOKEN);
        }
    }

    public Page<Message> getMessage(Long opentalkId, Pageable pageRequest){
        log.trace("MessageService > getOpentalkMessage()");
        // 오픈톡 ID로 opentlak 객체 찾기
        Opentalk opentalk = opentalkRepository.findByOpentalkId(opentalkId).orElseThrow(() -> new CustomException(ErrorCode.OPENTALK_NOT_FOUND));
        return messageRepository.findAllByOpentalk(opentalk, pageRequest);
    }

    public List<MessageResponseDto> pageToDto(Page<Message> page){
        log.trace("MessageService > pageToDto()");
        List<Message> messages = page.getContent();
        List<MessageResponseDto> messageList = new LinkedList<>();

        for(Message message : messages){
            messageList.add(messageMapper.convertToMessageResponseDto(message));
        }
        return messageList;
    }
}
