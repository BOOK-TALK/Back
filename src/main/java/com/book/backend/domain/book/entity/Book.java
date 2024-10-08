package com.book.backend.domain.book.entity;

import com.book.backend.domain.genre.entity.Genre;
import com.book.backend.domain.opentalk.entity.Opentalk;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String isbn;

    private String bookname;

    private String bookImageURL;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Opentalk opentalk;
}
