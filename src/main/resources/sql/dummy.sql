USE booktalk;
CREATE TABLE IF NOT EXISTS user
(
    birth_date date         null,
    gender     tinyint      null,
    reg_date   datetime(6)  null,
    user_id    bigint auto_increment
        primary key,
    apple_id   varchar(255) null,
    kakao_id   varchar(255) null,
    nickname   varchar(255) null,
    constraint UK792t1v1e9f43yusiryq37re13
        unique (kakao_id),
    constraint UKah2bnumq4fnjg70axxky0j5xs
        unique (nickname),
    constraint UKmnjg735lsf6wola6yyxxfj08l
        unique (apple_id),
    check (`gender` between 0 and 2)
);

CREATE TABLE IF NOT EXISTS book
(
    book_id       bigint auto_increment primary key,
    book_imageurl varchar(255) null,
    bookname      varchar(255) null,
    isbn          varchar(255) null
);

CREATE TABLE IF NOT EXISTS opentalk
(
    book_id     bigint not null,
    opentalk_id bigint auto_increment
        primary key,
    constraint UK2xjdoiqspesxa8oenlmyt52us
        unique (book_id),
    constraint FK3v2bbp8k2bt96wybtbk6pv9yp
        foreign key (book_id) references book (book_id)
);
CREATE TABLE IF NOT EXISTS message
(
    created_at  datetime(6)  null,
    message_id  bigint auto_increment
        primary key,
    opentalk_id bigint       null,
    user_id     bigint       null,
    content     varchar(255) null,
    constraint FK1iwnhnomrpim1phe1x0qlnva7
        foreign key (opentalk_id) references opentalk (opentalk_id),
    constraint FK2op594yomeg261726h4dj75jq
        foreign key (user_id) references user (user_id)
);
create index idx_message_createdAt
    on message (created_at);
CREATE TABLE IF NOT EXISTS `user dibs_books`
(
    user_id       bigint       not null,
    book_imageurl varchar(255) null,
    bookname      varchar(255) null,
    isbn          varchar(255) null,
    constraint FKjhhvi9pyxkt4w4ysu9o0b858x
        foreign key (user_id) references user (user_id)
);

CREATE TABLE IF NOT EXISTS user_libraries
(
    user_id bigint       not null,
    code    varchar(255) null,
    name    varchar(255) null,
    constraint FKcgk06cv5efj863hr3ffsa45r4
        foreign key (user_id) references user (user_id)
);
CREATE TABLE IF NOT EXISTS user_opentalk
(
    opentalk_id      bigint null,
    user_id          bigint null,
    user_opentalk_id bigint auto_increment
        primary key,
    constraint FK1klbjl6843fae6vli3yu88s0a
        foreign key (opentalk_id) references opentalk (opentalk_id),
    constraint FKhri45w7pin1p8n1drbpiy0bgh
        foreign key (user_id) references user (user_id)
);
CREATE TABLE IF NOT EXISTS user_read_books
(
    user_id       bigint       not null,
    book_imageurl varchar(255) null,
    bookname      varchar(255) null,
    isbn          varchar(255) null,
    constraint FKks39a5vkr0flfyih9t7qpl88n
        foreign key (user_id) references user (user_id)
);
CREATE TABLE IF NOT EXISTS goal
(
    goal_id     bigint auto_increment
        primary key,
    created_at  datetime(6)  null,
    is_finished bit          null,
    isbn        varchar(255) null,
    recent_page int          null,
    total_page  int          null,
    updated_at  datetime(6)  null,
    user_id     bigint       null,
    constraint FKcvxyydpkwvqex9uqphgc7a204
        foreign key (user_id) references user (user_id)
);
CREATE TABLE IF NOT EXISTS record
(
    record_id   bigint auto_increment
        primary key,
    date        datetime(6) null,
    recent_page int         null,
    goal_id     bigint      null,
    constraint FKnf7wid5lp179vslfjl7rgvv6s
        foreign key (goal_id) references goal (goal_id)
);

#------- dummy data -------
INSERT IGNORE INTO book (book_id, isbn)
 VALUES (1, '9788956055466'),
        (2, '9788994120966'),
        (3, '9788936433673'),
        (4, '9788956604992'),
        (5, '9788936434267'),
        (6, '9788965700609'),
        (7, '9788995151204'),
        (8, '9788954622035'),
        (9, '9788936433871'),
        (10, '9791195522125');

INSERT IGNORE INTO book (book_id, isbn, bookname, book_imageurl)
 VALUES (1, '9788956055466','책은 도끼다 :박웅현 인문학 강독회', 'https://bookthumb-phinf.pstatic.net/cover/067/378/06737822.jpg?type=m1&udate=20171111'),
        (2, '9788994120966', '지적 대화를 위한 넓고 얕은 지식', 'https://bookthumb-phinf.pstatic.net/cover/084/030/08403038.jpg?type=m1&udate=20171223'),
        (3, '9788936433673', '엄마를 부탁해 :신경숙 장편소설', 'http://image.aladin.co.kr/product/272/78/cover/8936433679_2.jpg'),
        (4, '9788956604992', '7년의 밤 :정유정 장편소설', 'http://image.aladin.co.kr/product/14712/55/cover/k202532053_1.jpg'),
        (5, '9788936434267', '아몬드손원평 장편소설', 'http://image.aladin.co.kr/product/16839/4/cover/k492534773_1.jpg'),
        (6, '9788965700609', '멈추면, 비로소 보이는 것들 :혜민 스님과 함께하는 내 마음 다시보기', 'https://bookthumb-phinf.pstatic.net/cover/068/064/06806489.jpg?type=m1&udate=20140911'),
        (7, '9788995151204', '난장이가 쏘아올린 작은 공:조세희 소설집', 'https://bookthumb-phinf.pstatic.net/cover/001/067/00106772.jpg?type=m1&udate=20130115'),
        (8, '9788954622035', '살인자의 기억법 :김영하 장편소설', 'https://bookthumb-phinf.pstatic.net/cover/072/622/07262295.jpg?type=m1&udate=20180314'),
        (9, '9788936433871', '두근두근 내 인생 :김애란 장편소설', 'http://image.aladin.co.kr/product/1190/50/cover/8936433873_2.jpg'),
        (10, '9791195522125', '언어의 온도 :말과 글에는 나름의 따뜻함과 차가움이 있다', 'http://image.aladin.co.kr/product/14842/6/cover/k742532452_1.jpg');

 INSERT IGNORE INTO opentalk (opentalk_id, book_id)
 VALUES (1, 1),
        (2, 2),
        (3, 3),
        (4, 4),
        (5, 5),
        (6, 6),
        (7, 7),
        (8, 8),
        (9, 9),
        (10, 10);

INSERT IGNORE INTO user (user_id, nickname, gender, birth_date, reg_date)
values (1, '유저0' ,'1', '1990-01-01', '2024-06-10'),
       (2, 'najin21', '2', '2000-03-16', '2024-07-22'),
       (3, '스릴98','1', '1995-05-20', '2024-08-15'),
       (4, 'navia', '2', '1992-07-25', '2024-09-30'),
       (5, '애벌레42', '1', '2003-09-30', '2024-10-10');

 # 즐찾 오픈톡
 INSERT IGNORE INTO user_opentalk(user_opentalk_id, user_id, opentalk_id)
 values (1, 1, 1),
        (2, 1, 2),
        (3, 2, 2),
        (4, 2, 4),
        (5, 2, 5);

 INSERT IGNORE INTO message (message_id, opentalk_id, user_id, content, created_at)
 VALUES (1, 1, 1, '안녕하세요! 오늘 읽은 책이 정말 흥미로웠어요.', '2024-06-10 00:00:00'),
        (2, 1, 2, '저도 그 책을 읽었어요. 특히 중반부가 인상 깊었어요.', '2024-06-10 00:01:00'),
        (3, 1, 3, '저는 그 책에서 얻은 통찰력이 정말 유익했어요.', '2024-06-10 00:02:00'),
        (4, 5, 4, '그 책의 결말이 정말 놀라웠죠!', '2024-06-10 00:03:00'),
        (5, 5, 5, '맞아요! 그 결말을 보고 한동안 생각이 멈추지 않았어요.', '2024-06-10 00:04:00'),
        (6, 1, 2, '작가는 정말 대단한 사람인 것 같아요.', '2024-06-10 00:05:00'),
        (7, 3, 3, '저도 동의해요. 그의 글쓰기 스타일이 독특해요.', '2024-06-10 00:06:00'),
        (8, 8, 4, '그렇죠? 다음 책이 정말 기대돼요.', '2024-06-10 00:07:00'),
        (9, 8, 5, '저도 기다리고 있어요! 언제 나올까요?', '2024-06-10 00:08:00'),
        (10, 8, 1, '출시일이 곧 발표된다고 들었어요.', '2024-06-10 00:09:00'),
        (11, 10, 2, '책을 읽으며 느낀 점이 정말 많아요.', '2024-06-10 00:10:00'),
        (12, 10, 3, '맞아요. 제 인생에 큰 영향을 줬어요.', '2024-06-10 00:11:00'),
        (13, 1, 4, '책에서 다룬 주제가 현실적이어서 더 와닿았어요.', '2024-06-10 00:12:00'),
        (14, 4, 5, '맞아요. 현실적인 문제들을 잘 다루었죠.', '2024-06-10 00:13:00'),
        (15, 2, 1, '그 부분이 정말 마음에 들었어요.', '2024-06-10 00:14:00'),
        (16, 2, 2, '다음 책에서는 더 깊이 다뤘으면 좋겠어요.', '2024-06-10 00:15:00'),
        (17, 2, 3, '그러게요. 더 깊이 있는 내용이 나오면 좋겠어요.', '2024-06-10 00:16:00'),
        (18, 3, 4, '다음 책은 언제쯤 나올까요?', '2024-06-10 00:17:00'),
        (19, 4, 5, '곧 발표될 거라고 들었어요.', '2024-06-10 00:18:00'),
        (20, 5, 1, '빨리 나왔으면 좋겠어요!', '2024-06-10 00:19:00'),
        (21, 1, 1, '안녕하세요. 최근에 읽은 책이 있나요?', '2024-06-10 00:20:00'),
        (22, 2, 2, '네, 저는 "해리포터"를 다시 읽었어요.', '2024-06-10 00:21:00'),
        (23, 10, 3, '아, 그 책 정말 재밌죠. 저는 지금 "반지의 제왕"을 읽고 있어요.', '2024-06-10 00:22:00'),
        (24, 9, 4, '저도 "반지의 제왕" 좋아해요! 특히 영화도 멋지더라고요.', '2024-06-10 00:23:00'),
        (25, 9, 5, '맞아요. 그 영화는 정말 장관이었죠.', '2024-06-10 00:24:00'),
        (26, 1, 2, '요즘에는 어떤 책을 읽고 계신가요?', '2024-06-10 00:25:00'),
        (27, 1, 3, '저는 "호밀밭의 파수꾼"을 읽고 있어요. 분위기가 독특하네요.', '2024-06-10 00:30:00'),
        (28, 3, 4, '그 책은 좀 어두운 면이 있지만 생각할 거리를 많이 주죠.', '2024-06-10 00:35:00'),
        (29, 2, 5, '네, 주인공의 심리 상태가 흥미로워요.', '2024-06-10 00:40:00'),
        (30, 2, 1, '최근에는 "셜록 홈즈" 시리즈를 읽고 있어요.', '2024-06-10 00:45:00'),
        (31, 1, 2, '셜록 홈즈는 클래식이죠. 사건의 전개가 정말 흥미롭잖아요.', '2024-06-10 00:50:00'),
        (32, 2, 3, '맞아요. 매번 결말이 예상치 못한 방향으로 가서 놀라워요.', '2024-06-10 00:55:00'),
        (33, 3, 4, '혹시 탐정 소설 중에 추천할 만한 책이 있을까요?', '2024-06-10 01:00:00'),
        (34, 4, 5, '저는 "아가사 크리스티"의 작품들을 추천드려요.', '2024-06-10 01:05:00'),
        (35, 5, 1, '그분의 작품은 정말 수수께끼 같죠.', '2024-06-10 01:10:00'),
        (36, 5, 3, '특히 "오리엔트 특급 살인"은 최고예요.', '2024-06-10 01:15:00'),
        (37, 5, 4, '맞아요, 결말이 정말 충격적이었어요.', '2024-06-10 01:20:00'),
        (38, 8, 5, '혹시 다른 추천할 만한 책은?', '2024-06-10 01:25:00'),
        (39, 6, 1, '저는 "대지"라는 책을 추천합니다.', '2024-06-10 01:30:00'),
        (40, 5, 2, '"대지"는 어떤 내용인가요?', '2024-06-10 01:35:00'),
        (41, 5, 3, '주로 중국 농민의 삶을 다루고 있어요. 정말 감동적이에요.', '2024-06-10 01:40:00'),
        (42, 2, 4, '그렇군요. 한번 읽어봐야겠어요.', '2024-06-10 01:45:00'),
        (43, 2, 5, '시간 되면 꼭 읽어보세요.', '2024-06-10 01:50:00'),
        (44, 2, 1, '요즘 어떤 책을 읽고 있나요?', '2024-06-10 01:55:00'),
        (45, 5, 2, '저는 "바람과 함께 사라지다"를 읽고 있어요.', '2024-06-10 02:00:00'),
        (46, 1, 3, '그 책은 진짜 클래식이죠.', '2024-06-10 02:05:00'),
        (47, 2, 4, '네, 정말 흡입력이 있어요.', '2024-06-10 02:10:00'),
        (48, 3, 5, '주인공의 캐릭터가 정말 인상적이죠.', '2024-06-10 02:15:00'),
        (49, 4, 1, '맞아요, 특히 스칼렛의 캐릭터가 강렬해요.', '2024-06-10 02:20:00'),
        (50, 10, 2, '저는 사실 그 책을 읽고 눈물을 흘렸어요.', '2024-06-10 02:25:00'),
        (51, 10, 3, '그만큼 감동적인 책이죠.', '2024-06-10 02:30:00'),
        (52, 10, 4, '다른 추천할 만한 책이 있나요?', '2024-06-10 02:35:00'),
        (53, 10, 5, '저는 "모비딕"을 추천합니다.', '2024-06-10 02:40:00'),
        (54, 10, 1, '그 책은 정말 심오하죠.', '2024-06-10 02:45:00'),
        (55, 9, 2, '주인공의 집념이 대단해요.', '2024-06-10 02:50:00'),
        (56, 9, 3, '맞아요, 정말 깊이 생각하게 만드는 책이에요.', '2024-06-10 02:55:00'),
        (57, 9, 4, '어떤 책을 읽고 계신가요?', '2024-06-10 03:00:00'),
        (58, 9, 5, '저는 "파리대왕"을 읽고 있어요.', '2024-06-10 03:05:00'),
        (59, 9, 1, '그 책은 정말 충격적이죠.', '2024-06-10 03:10:00'),
        (60, 9, 2, '네, 인간 본성에 대해 다시 생각하게 돼요.', '2024-06-10 03:15:00'),
        (61, 10, 3, '특히 마지막 장면이 잊혀지질 않아요.', '2024-06-10 03:20:00'),
        (62, 10, 4, '맞아요, 정말 강렬하죠.', '2024-06-10 03:25:00'),
        (63, 3, 5, '또 다른 책 추천이 있을까요?', '2024-06-10 03:30:00'),
        (64, 4, 1, '저는 "작은 아씨들"을 추천해요.', '2024-06-10 03:35:00'),
        (65, 5, 2, '그 책은 정말 따뜻하죠.', '2024-06-10 03:40:00'),
        (66, 6, 3, '가족의 사랑이 잘 표현된 책이에요.', '2024-06-10 03:45:00'),
        (67, 6, 4, '네, 정말 감동적이었어요.', '2024-06-10 03:50:00'),
        (68, 8, 5, '저는 사실 그 책을 읽고 행복해졌어요.', '2024-06-10 03:55:00'),
        (69, 8, 1, '그만큼 따뜻한 책이죠.', '2024-06-10 04:00:00'),
        (70, 7, 2, '요즘 또 다른 책을 읽고 계신가요?', '2024-06-10 04:05:00'),
        (71, 7, 3, '저는 "셜록 홈즈" 시리즈에 빠져 있어요.', '2024-06-10 04:10:00'),
        (72, 7, 1, '안녕하세요! 책 읽는 것을 좋아하세요?', '2024-06-10 04:12:00'),
        (73, 7, 2, '네, 특히 소설을 좋아합니다. 최근에 읽은 책은 무엇인가요?', '2024-06-10 04:13:00'),
        (74, 7, 3, '저는 최근에 "해리포터"를 다시 읽었어요.', '2024-06-10 04:14:00'),
        (75, 7, 4, '해리포터 정말 좋죠! 저는 "반지의 제왕"도 좋아해요.', '2024-06-10 04:15:00'),
        (76, 7, 5, '역시 판타지 장르가 최고인 것 같아요.', '2024-06-10 04:16:00'),
        (77, 5, 2, '맞아요, 다음엔 "나니아 연대기"를 읽어볼까 해요.', '2024-06-10 04:17:00'),
        (78, 6, 3, '좋은 선택이에요! 이야기의 전개가 정말 흥미로워요.', '2024-06-10 04:18:00'),
        (79, 4, 4, '다들 다양한 책을 읽는 것 같아 정말 좋네요.', '2024-06-10 04:19:00'),
        (80, 4, 5, '책을 읽으면 다른 세계에 빠져드는 것 같아요.', '2024-06-10 04:20:00'),
        (81, 5, 1, '정말 그렇죠! 읽고 나면 생각도 깊어지는 것 같아요.', '2024-06-10 04:21:00'),
        (82, 3, 2, '저도 그 책을 좋아해요. 전개가 정말 훌륭하죠.', '2024-06-10 07:31:00'),
        (83, 4, 4, '이야기의 흐름이 매력적이에요. 중간에 멈출 수가 없었어요.', '2024-06-10 07:32:00'),
        (84, 9, 1, '맞아요, 저도 시간 가는 줄 모르고 읽었어요.', '2024-06-10 07:33:00'),
        (85, 9, 1, '안녕하세요, 오늘 어떤 책을 읽으셨나요?', '2024-06-10 07:35:00'),
        (86, 10, 3, '저는 오늘 "자바의 정석"을 읽었어요.', '2024-06-10 07:36:00'),
        (87, 10, 2, '오, 저도 그 책 좋아해요. 특히 5장의 내용이 인상적이었어요.', '2024-06-10 07:37:00'),
        (88, 10, 4, '혹시 "클린 코드"도 읽어보셨나요?', '2024-06-10 07:38:00'),
        (89, 8, 5, '네, 클린 코드는 정말 유익한 책이었어요.', '2024-06-10 07:39:00'),
        (90, 7, 2, '요즘 읽고 있는 책은 무엇인가요?', '2024-06-10 07:40:00'),
        (91, 6, 4, '저는 "도메인 주도 설계"를 읽고 있어요.', '2024-06-10 07:41:00'),
        (92, 8, 1, '그 책도 좋죠. 설계 패턴에 대해 많은 것을 배울 수 있었어요.', '2024-06-10 07:42:00'),
        (93, 8, 5, '최근에 추천받은 책이 있나요?', '2024-06-10 07:43:00'),
        (94, 8, 3, '네, "Effective Java"를 추천받았어요.', '2024-06-10 07:44:00'),
        (95, 6, 5, '저도 그 책 읽고 있어요! 정말 유익해요.', '2024-06-10 07:45:00'),
        (96, 2, 2, '그 책을 통해 자바의 깊이를 더 이해하게 되었어요.', '2024-06-10 07:46:00'),
        (97, 3, 4, '혹시 최근에 읽은 책 중에서 제일 기억에 남는 책은 무엇인가요?', '2024-06-10 07:47:00'),
        (98, 4, 1, '"소프트웨어 아키텍처"라는 책이 정말 유익했어요.', '2024-06-10 07:48:00'),
        (99, 5, 3, '맞아요, 그 책은 개발자라면 꼭 읽어야 할 책이죠.', '2024-06-10 07:49:00'),
        (100, 1, 4, '혹시 다음에 읽고 싶은 책이 있나요?', '2024-06-10 07:50:00'),
        (101, 2, 1, '"리팩토링"을 읽고 싶어요. 코드 개선에 많은 도움이 될 것 같아요.', '2024-06-10 07:51:00'),
        (102, 3, 5, '그 책도 정말 유익해요. 실전에서 많이 참고하고 있어요.', '2024-06-10 07:52:00'),
        (103, 5, 2, '자바와 관련된 좋은 책이 또 있을까요?', '2024-06-10 07:53:00'),
        (104, 5, 3, '"자바 병렬 프로그래밍"도 추천할 만해요.', '2024-06-10 07:54:00'),
        (105, 1, 5, '감사합니다. 다음에 꼭 읽어볼게요.', '2024-06-10 07:55:00'),
        (106, 2, 4, '읽으면서 궁금한 점이 생기면 같이 토론해요.', '2024-06-10 07:56:00'),
        (107, 10, 1, '좋아요! 함께 공부하는 게 더 재미있어요.', '2024-06-10 07:57:00'),
        (108, 10, 2, '이렇게 책에 대해 이야기 나누는 시간이 정말 좋아요.', '2024-06-10 07:58:00'),
        (109, 8, 3, '저도요, 서로에게 많은 도움이 되는 것 같아요.', '2024-06-10 07:59:00'),
        (109, 8, 2, '오늘 하루도 화이팅입니다!', '2024-06-10 07:59:00');

INSERT IGNORE INTO goal (goal_id, isbn, user_id, total_page, created_at, updated_at, is_finished)
VALUES (1, '9788936434267', 4, 100, '2024-06-7 12:34:00', '2024-06-11 23:11:00', 0),
       (2, '9788936434267', 1, 100, '2024-06-9 14:11:04', '2024-06-12 13:12:10', 0),
       (3, '9788936434267', 2, 100, '2024-06-10 09:53:17', '2024-06-11 19:40:10', 0),
       (4, '9788936434267', 3, 100, '2024-06-10 11:15:30', '2024-06-11 21:10:07', 0);

INSERT IGNORE INTO record (record_id, goal_id, date, recent_page)
VALUES (1, 1, '2024-06-7', 14),
       (2, 2, '2024-06-9', 10),
       (3, 3, '2024-06-10', 5),
       (4, 4, '2024-06-10', 11),
       (5, 1, '2024-06-11', 28),
       (6, 3, '2024-06-11', 14),
       (7, 4, '2024-06-11', 41),
       (8, 1, '2024-06-12', 30),
       (9, 2, '2024-06-12', 42),
       (10, 3, '2024-06-12', 17);
