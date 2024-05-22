package com.prj2spring.controller.member;

import com.prj2spring.domain.member.Member;
import com.prj2spring.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @PostMapping("signup")
    public void signup(@RequestBody Member member) {
        service.add(member);
    }

    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(@RequestParam String email) {
        Member member = service.getByEmail(email);
        if (member == null) { // null -> 사용할 수 있는 이메일
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member.getEmail()); // DB 있으니 중복
    }

    @GetMapping(value = "check", params = "nickName")
    public ResponseEntity checkNickName(@RequestParam String nickName) {
        Member member = service.getByNickName(nickName);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nickName);
    }
}
