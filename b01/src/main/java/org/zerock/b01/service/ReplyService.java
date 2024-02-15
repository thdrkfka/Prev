package org.zerock.b01.service;

import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;

public interface ReplyService {//인터페이스 => 추상메소드!!

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
