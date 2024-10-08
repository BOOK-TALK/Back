package com.book.backend.domain.opentalk.service;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.book.repository.BookRepository;
import com.book.backend.domain.message.dto.MessageResponseDto;
import com.book.backend.domain.detail.service.DetailService;
import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.message.repository.MessageRepository;
import com.book.backend.domain.message.service.MessageService;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.DetailResponseDto;
import com.book.backend.domain.opentalk.dto.OpentalkDto;
import com.book.backend.domain.opentalk.dto.OpentalkJoinResponseDto;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.domain.userOpentalk.entity.UserOpentalk;
import com.book.backend.domain.userOpentalk.repository.UserOpentalkRepository;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OpentalkService {
    private final UserOpentalkRepository userOpentalkRepository;
    private final MessageRepository messageRepository;
    private final OpentalkRepository opentalkRepository;
    private final BookRepository bookRepository;
    private final DetailService detailService;
    private final UserService userService;
    private final OpentalkResponseParser opentalkResponseParser;
    private final MessageService messageService;

    /* message 테이블에서 최근 200개 데이터 조회 -> opentalkId 기준으로 count 해서 가장 빈번하게 나오는 top 5 id 반환*/
    public List<Long> getHotOpentalkIds() {
        log.trace("OpentalkService > hotOpentalk()");
        List<Message> recent200Messages = messageRepository.findTop50ByOrderByCreatedAtDesc();

        // (key : opentalk_id, value : 출현빈도)
        Map<Long, Long> opentalkIdCountMap = recent200Messages.stream().collect(
                Collectors.groupingBy(message -> message.getOpentalk().getOpentalkId(), Collectors.counting())
        );
        // value 순으로 정렬해서 top 5 id 반환
        return opentalkIdCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    // 오픈톡 즐찾 추가
    @Transactional
    public List<Long> addFavoriteOpentalk(Long opentalkId) {
        log.trace("OpentalkService > addFavoriteOpentalk()");
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        Opentalk opentalk = opentalkRepository.findById(opentalkId).orElseThrow(() -> new CustomException(ErrorCode.OPENTALK_NOT_FOUND));

        if(userOpentalkRepository.findByUserIdAndOpentalkId(user, opentalk) != null) {
            throw new CustomException(ErrorCode.ALREADY_EXIST);
        }
        UserOpentalk userOpentalk = new UserOpentalk();
        userOpentalk.setOpentalkId(opentalk);
        userOpentalk.setUserId(user);

        userOpentalkRepository.save(userOpentalk);
        return getFavoriteOpentalkIds();
    }

    // 오픈톡 즐찾 삭제
    @Transactional
    public List<Long> deleteFavoriteOpentalk(Long opentalkId) {
        log.trace("OpentalkService > deleteFavoriteOpentalk()");
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        Opentalk opentalk = opentalkRepository.findById(opentalkId).orElseThrow(() -> new CustomException(ErrorCode.OPENTALK_NOT_FOUND));

        UserOpentalk userOpentalk = userOpentalkRepository.findByUserIdAndOpentalkId(user, opentalk);
        if(userOpentalk == null) {
            throw new CustomException(ErrorCode.USER_OPENTALK_NOT_FOUND);
        }
        userOpentalkRepository.delete(userOpentalk);
        return getFavoriteOpentalkIds();
    }

    /* 해당 user의 즐찾 opentalk list 반환*/
    public List<Long> getFavoriteOpentalkIds() {
        log.trace("OpentalkService > favoriteOpentalk()");
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        List<UserOpentalk> opentalkList = userOpentalkRepository.findAllByUserId(user);

        List<Long> opentalkIds = new LinkedList<>();
        for(UserOpentalk userOpentalk : opentalkList) {
            opentalkIds.add(userOpentalk.getOpentalkId().getOpentalkId());
        }
        return opentalkIds;
    }

    // 해당 오픈톡 id의 bookname, bookImageURL 불러오기
    public List<OpentalkDto> getBookInfo(List<Long> opentalkId) throws Exception {
        log.trace("OpentalkService > getBookInfo()");
        List<OpentalkDto> opentalkDtoList = new LinkedList<>();

        for(Long id : opentalkId) {
            // bookrepository 에서 opentalkid 로 찾기
            Optional<Opentalk> opentalk = opentalkRepository.findByOpentalkId(id);
            Book book = opentalk.get().getBook();
            OpentalkDto opentalkDto = OpentalkDto.builder()
                    .id(id)
                    .isbn13(book.getIsbn())
                    .bookName(book.getBookname())
                    .bookImageURL(book.getBookImageURL())
                    .build();
            opentalkDtoList.add(opentalkDto);
        }
        return opentalkDtoList;
    }


    // 오픈톡 참여하기
    @Transactional
    public OpentalkJoinResponseDto joinOpentalk(String isbn, String bookname, String bookImageURL, int pageSize){
        log.trace("OpentalkService > joinOpentalk()");
        Opentalk opentalk = checkExistOpentalk(isbn);
        if(opentalk == null){
            opentalk = createOpentalkByIsbn(isbn, bookname, bookImageURL);
            return OpentalkJoinResponseDto.builder().opentalkId(opentalk.getOpentalkId()).messageResponseDto(null).isFavorite(false).build();
        }
        // 오픈톡이 존재하는 경우
        Pageable pageRequest = PageRequest.of(0, pageSize, Sort.by("createdAt").descending());
        Page<Message> messagePage = messageService.getMessage(opentalk.getOpentalkId(), pageRequest);
        List<MessageResponseDto> response = messageService.pageToDto(messagePage);

        // 즐찾 여부
        User user = userService.loadLoggedinUser();
        boolean isFavorite = userOpentalkRepository.findByUserIdAndOpentalkId(user, opentalk) != null;

        return OpentalkJoinResponseDto.builder()
                .opentalkId(opentalk.getOpentalkId())
                .messageResponseDto(response)
                .isFavorite(isFavorite)
                .build();
    }


    @Transactional
    public Opentalk createOpentalkByIsbn(String isbn, String bookname, String bookImageURL) {
        log.trace("OpentalkService > createOpentalkByIsbn");
        Book book = new Book();
        book.setIsbn(isbn);
        book.setBookname(bookname);
        book.setBookImageURL(bookImageURL);
        bookRepository.save(book);

        Opentalk opentalk = new Opentalk();
        opentalk.setBook(book);
        return opentalkRepository.save(opentalk);
    }

    public Opentalk checkExistOpentalk(String isbn) {
        log.trace("OpentalkService > checkExistOpentalk()");
        Book book = bookRepository.findByIsbn(isbn);
        if(book == null) return null;
        return opentalkRepository.findByBook(book);
    }
}
