package com.prj2spring.domain.board;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Board {
    private Integer id;
    private String title;
    private String content;
    private String writer; // 작성자 nickName 으로 활용 예정
    private Integer memberId; // foreign key 역할
    private LocalDateTime inserted;

    private Integer numberOfImages; // 이미지 유무 게시글에 표시를 위한 용도
    private List<String> imageSrcList; // 이미지 출력을 위해 이미지 경로 저장된 리스트
}
