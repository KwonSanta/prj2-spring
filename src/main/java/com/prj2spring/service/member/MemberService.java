package com.prj2spring.service.member;

import com.prj2spring.domain.member.Member;
import com.prj2spring.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberService {

    final MemberMapper mapper;
    final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public void add(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setEmail(member.getEmail().trim());
        member.setNickName(member.getNickName().trim());

        mapper.insert(member);
    }

    public Member getByEmail(String email) {
        return mapper.selectByEmail(email.trim());
    }

    public Member getByNickName(String nickName) {
        return mapper.selectByNickName(nickName.trim());
    }

    public boolean validate(Member member) {
        if (member.getEmail() == null || member.getEmail().isBlank()) {
            return false;
        }

        if (member.getNickName() == null || member.getNickName().isBlank()) {
            return false;
        }

        if (member.getPassword() == null || member.getPassword().isBlank()) {
            return false;
        }

        String emailPattern = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*";

        if (!member.getEmail().trim().matches(emailPattern)) {
            return false;
        }

        return true;
    }

    public List<Member> list() {
        return mapper.selectAll();
    }

    public Member getById(Integer id) {
        return mapper.selectById(id);
    }

    public void remove(Integer id) {
        mapper.deleteById(id);
    }

    public boolean hasAccess(Member member, Authentication authentication) {
        Member dbMember = mapper.selectById(member.getId());

        if (!member.getId().toString().equals(authentication.getName())) {
            return false;
        }


        if (dbMember == null) {
            return false;
        }

        return passwordEncoder.matches(member.getPassword(), dbMember.getPassword());
    }

    public boolean hasAccessModify(Member member) {
        Member dbMember = mapper.selectById(member.getId());
        if (dbMember == null) {
            return false;
        }

        if (!passwordEncoder.matches(member.getOldPassword(), dbMember.getPassword())) {
            return false;
        }

        return true;
    }

    public void modify(Member member) {
        if (member.getPassword() != null && member.getPassword().length() > 0) {
            // 패스워드가 입력되었으니 바꾸기
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        } else {
            // 입력 안됐으니 기존값(dbMember id로 조회해서)으로 유지
            Member dbMember = mapper.selectById(member.getId());
            member.setPassword(dbMember.getPassword());
        }
        mapper.update(member);
    }


    public Map<String, Object> getToken(Member member) {
        // 결과를 저장할 Map을 선언합니다. 초기에는 null로 설정됩니다.
        Map<String, Object> result = null;

        // 데이터베이스에서 이메일을 기준으로 회원 정보를 조회합니다.
        Member db = mapper.selectByEmail(member.getEmail());
        if (db != null) {
            // 조회된 회원 정보가 있을 경우, 입력된 비밀번호와 저장된 비밀번호를 비교합니다.
            if (passwordEncoder.matches(member.getPassword(), db.getPassword())) {
                // 비밀번호가 일치하면 새로운 HashMap을 생성하여 결과를 저장합니다.
                result = new HashMap<>();
                String token = ""; // 토큰을 저장할 문자열을 선언합니다.
                Instant now = Instant.now(); // 현재 시간을 저장합니다.

                // JWT 클레임을 설정합니다. 클레임은 JWT 토큰에 포함될 정보들입니다.
                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self") // 토큰 발급자
                        .issuedAt(now) // 토큰 발급 시간
                        .expiresAt(now.plusSeconds(60 * 60 * 24 * 7)) // 토큰 만료 시간 (1주일 후)
                        .subject(db.getId().toString()) // 토큰의 주체(주로 사용자 식별자)
                        .claim("scope", "") // todo : "" -> 권한 명세 필요 // 추가 클레임 (예: 사용자 권한). 현재는 빈 문자열
                        .claim("nickName", db.getNickName()) // 사용자의 닉네임을 클레임에 추가
                        .build();
                // JWT 인코더를 사용하여 설정된 클레임으로 JWT 토큰을 생성합니다.
                token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
                // 생성된 토큰을 결과 Map에 추가합니다.
                result.put("token", token);
            }
        }
        // 결과 Map을 반환합니다. 비밀번호가 일치하지 않거나 사용자가 존재하지 않을 경우 null을 반환합니다.
        return result;
    }
}