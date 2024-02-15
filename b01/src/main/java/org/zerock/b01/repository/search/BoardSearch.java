package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.upload.BoardListAllDTO;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);//페이지 처리 기능
    
    //검색 조건들(types)과 키워드(keyword), 여러조건의 조합이 가능하도록 처리하는 메소드
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);


    //목록 데이터 처리하기 위해서 Querydsl 이용하기 위해 메소드 추가
    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);

}
