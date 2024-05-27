USE prj2;

# 게시물 테이블 생성
CREATE TABLE board
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    title    VARCHAR(100)  NOT NULL,
    content  VARCHAR(1000) NOT NULL,
    writer   VARCHAR(100)  NOT NULL,
    inserted DATETIME      NOT NULL DEFAULT NOW()
);

SELECT *
FROM board
ORDER BY id DESC;

# 회원 테이블 생성
CREATE TABLE member
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    email     VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    nick_name VARCHAR(100) NOT NULL UNIQUE,
    inserted  DATETIME     NOT NULL DEFAULT NOW()
)

DESC board;
SELECT *
FROM member
ORDER BY id DESC;

# board 테이블 수정
# writer column 지우기
# member_id column reference member(id)

ALTER TABLE board
    DROP COLUMN writer;

ALTER TABLE board
    ADD COLUMN member_id INT REFERENCES member (id) AFTER content;

UPDATE board
SET member_id = (SELECT id FROM member ORDER BY id DESC LIMIT 1)
WHERE id > 0;

ALTER TABLE board
    MODIFY COLUMN member_id INT NOT NULL;

DESC board;

SELECT *
FROM board
ORDER BY id DESC;

#
SELECT *
FROM member
WHERE id = 9;

DELETE
FROM board
WHERE member_id = 9;
DELETE
FROM member
WHERE id = 9;

# 권한 테이블
CREATE TABLE authority
(
    member_id INT         NOT NULL REFERENCES member (id),
    name      VARCHAR(20) NOT NULL,
    PRIMARY KEY (member_id, name)
);

INSERT INTO authority (member_id, name)
VALUES (21, 'admin');

SELECT *
FROM authority;

SELECT *
FROM member
ORDER BY id DESC ;

# Pagination
# 게시물 복붙해서 더미 데이터 만들기
INSERT INTO board
    (title, content, member_id)
SELECT title, content, member_id
FROM board;

SELECT *
FROM member
WHERE id = 21;