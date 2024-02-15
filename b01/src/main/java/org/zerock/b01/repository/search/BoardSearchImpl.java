package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.upload.BoardImageDTO;
import org.zerock.b01.dto.upload.BoardListAllDTO;

import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {//실제 구현 클래스

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        //Q도메인 객체
        QBoard board = QBoard.board;

        //select... from board
        JPQLQuery<Board> query = from(board);

        BooleanBuilder booleanBuilder = new BooleanBuilder(); //(
        //where 조건
        //or
        booleanBuilder.or(board.title.contains("1"));//title like..
        booleanBuilder.or(board.content.contains("1"));//content like..

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));//bno 가 0보다 큰

//        //where title like...
//        query.where(board.title.contains("1"));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;//Q도메인 객체 생성//QBoard board = new QBoard("board");

        JPQLQuery<Board> query = from(board);//select... from board

        //where
        //검색조건(types)과 키워드(keyword)가 있다면
        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();//(

            for(String type: types){
                switch (type){
                    case "t": //case1: 검색조건 - 제목(title)
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c": //case2: 검색조건 - 내용(content)
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w": //case3: 검색조건 - 작성자(writer)
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;

                }
            }//for문 끝
            query.where(booleanBuilder);
        }//if문 끝

        //bno > 0
        query.where(board.bno.gt(0L));

        //paging 처리 //하드에 저장된 데이터베이스를 메모리에 저장해서 웹으로 보여주기 위함.
        this.getQuerydsl().applyPagination(pageable, query);
        //count 쿼리 실행
        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        //Q도메인 객체 생성//QBoard board = new QBoard("board");
        QBoard board = QBoard.board;
        //Q도메인 객체 생성//QReply reply = new QReply("reply");
        QReply reply = QReply.reply;

        //select... from board
        JPQLQuery<Board> query = from(board);

        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        // 검색 조건이 주어진 경우
        if ((types != null && types.length > 0) && keyword != null){

            BooleanBuilder booleanBuilder = new BooleanBuilder();//(

            // 검색 조건에 따라 OR 연산 수행
            for (String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }

            }

            query.where(booleanBuilder);

        }

        //bno > 0
        query.where(board.bno.gt(0L));

        // select 문에 Projections 사용하여 DTO 생성
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.
                bean(BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")
        ));

        // 페이징 및 정렬 정보 적용
        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        // 쿼리 실행 및 결과 반환
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);

    }

//    @Override
//    public Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); //left join
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging
//
//        List<Board> boardList = boardJPQLQuery.fetch();
//
//        boardList.forEach(board1 -> {
//            System.out.println(board1.getBno());
//            System.out.println(board1.getImageSet());
//            System.out.println("------------------");
//        });
//
//        return null;
//    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); //left join

        if ((types != null && types.length >0) && keyword != null) {

            BooleanBuilder booleanBuilder = new BooleanBuilder(); //(

            for (String type: types) {

                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            boardJPQLQuery.where(booleanBuilder);
        }

        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {

            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            //BoardImage를 BoardImageDTO 처리할 부분
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                    ).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS); //처리된 BoardImageDTO들을 추가

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
