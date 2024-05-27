package com.prj2spring.service.board;

import com.prj2spring.domain.board.Board;
import com.prj2spring.mapper.board.BoardMapper;
import com.prj2spring.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;
    private final MemberMapper memberMapper;

    public void add(Board board, Authentication authentication) {
        board.setMemberId(Integer.valueOf(authentication.getName()));
        mapper.insert(board);
    }

    public boolean validate(Board board) {
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }

        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }

        return true;
    }

    public Map<String, Object> list(Integer page) {
        Map pageInfo = new HashMap();

        Integer firstPage = 1;
        Integer countAll = mapper.countAll();

        Integer offset = (page - 1) * 10;
        Integer lastPageNumber = (countAll - 1) / 10 + 1;
        Integer leftPageNumber = (page - 1) / 10 * 10 + 1;
        Integer rightPageNumber = leftPageNumber + 9;
        rightPageNumber = Math.min(rightPageNumber, lastPageNumber);
        // 이전
        Integer prevPggeNumber = leftPageNumber - 1;
        // 다음
        Integer nextPggeNumber = rightPageNumber + 1;

        // 처음, 이전, 다음, 맨끝
        if (prevPggeNumber > 0) {
            pageInfo.put("prevPggeNumber", prevPggeNumber);
        }
        if (nextPggeNumber <= lastPageNumber) {
            pageInfo.put("nextPggeNumber", nextPggeNumber);
        }
        pageInfo.put("currentPageNumber", page);
        pageInfo.put("lastPageNumber", lastPageNumber);
        pageInfo.put("leftPageNumber", leftPageNumber);
        pageInfo.put("rightPageNumber", rightPageNumber);
        pageInfo.put("firstPageNumber", firstPage);
        pageInfo.put("countAll", countAll);

        return Map.of("pageInfo", pageInfo,
                "boardList", mapper.selectAllPaging(offset));
    }

    public Board get(Integer id) {
        return mapper.selectById(id);
    }

    public void remove(Integer id) {

        mapper.deleteById(id);
    }

    public void edit(Board board) {
        mapper.update(board);
    }

    public boolean hasAccess(Integer id, Authentication authentication) {
        Board board = mapper.selectById(id);

        return board.getMemberId()
                .equals(Integer.valueOf(authentication.getName()));
    }
}