package com.book.backend.domain.user.repository;

import com.book.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(final String loginId);

    Optional<User> findByKakaoId(final String kakaoId);

    Optional<User> findByAppleId(final String appleId);

}
