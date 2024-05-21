package com.prj2spring.controller.board;

import com.prj2spring.domain.board.Board;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Controller + ResponseBody
@RequestMapping("/api/board")
public class BoardController {

    @PostMapping("add")
    public void add(@RequestBody Board board) {
        System.out.println("board = " + board);
    }
}
