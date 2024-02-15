package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Reply;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test//댓글 입력 테스트
    public void testInsert() {

        //실제 DB에 있는 bno
        Long bno = 112L;

        Board board = Board.builder()
                .bno(bno)
                .build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("고선생이랑 치즈 보고싶다ㅏㅏㅏㅏㅏ")
                .replyer("replyer01")
                .build();

        replyRepository.save(reply);

    }

    @Transactional//p.539 참고
    @Test
    public void testBoardReplies() {

        Long bno = 200L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> {
            log.info(reply);
        });
    }
}
