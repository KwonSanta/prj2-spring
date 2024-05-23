package com.prj2spring.domain.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private Integer id;
    private String title;
    private String content;
    private String writer; // 작성자 nickName으로 활용 예정
    private Integer memberId; // foreign key 역할
    private LocalDateTime inserted;
}
