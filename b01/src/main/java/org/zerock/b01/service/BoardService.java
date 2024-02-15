package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.upload.BoardListAllDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    //등록 처리
    Long register(BoardDTO boardDTO);//중괄호 X, 추상메소드 - 오버라이딩 하기 위해

    //조회 작업 처리
    BoardDTO readOne(Long bno);

    //수정 작업 처리
    void modify(BoardDTO boardDTO);

    //삭제 작업 처리
    void remove(Long bno);
    
    //list()라는 이름으로 목록/검색 기능 선언
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    
    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);


    //DTO를 Entity로 변환하기//p.640~
    //게시물 등록 처리
    default Board dtoToEntity(BoardDTO boardDTO) {

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())

                .build();

        if (boardDTO.getFileNames() != null) {
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    //게시물 조회 처리
    default BoardDTO entityToDTO(Board board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid() +"_" +boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}
