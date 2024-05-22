package com.prj2spring.domain.member;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Member {

    private Integer id;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime inserted;

    // 가입일시 2024-05-22T14:41:02 -> 년/월/일로
    // getMethod 는 react 에서 property 로 사용 가능
    public String getSignupDateAndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        return inserted.format(formatter);
    }
}
