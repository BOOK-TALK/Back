package com.book.backend.domain.opentalk.entity;

import com.book.backend.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "opentalk")
@Getter
@Setter
public class Opentalk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opentalkId;

    @OneToOne
    @JoinColumn(name = "book_id", nullable = false) // isbn 아니고 DB ID
    private Book book;

    // 연관관계 편의 메서드 (서비스 계층에서 관리할 시 삭제할 것)
    public void setBook(Book book) {
        this.book = book;
        if (book.getOpentalk() != this) {
            book.setOpentalk(this);
        }
    }
}
