package org.zerock.b01.domain;


import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity//데이터에 해당하는 객체 엔티티
@Getter//가져오기
@Builder//빌더 패턴 쓰려고
@AllArgsConstructor//파라미터 있는 생성자
@NoArgsConstructor//기본 생성자
@ToString(exclude = "imageSet")//필드명 한번에 다 빼오려고//exclude ="~"//~만 뺴고
public class Board extends BaseEntity{//테이블 이름 = 클래스 이름

    @Id//PK(기본키) 갖는 객체
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //키 생성 전략, IDENTITY : 데이터베이스에서 알아서 결정하라..
    private Long bno;

    @Column(length = 500, nullable = false)//칼럼의 길이와 null 허용여부
    private String title;//테이블 속성 타입 = 각 필드의 데이터 타입

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;
    
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    @OneToMany(mappedBy = "board",//BoardImage의 board 변수//Board 클래스에 연관관계 부여
                cascade = {CascadeType.ALL},
                fetch = FetchType.LAZY,
                orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)//N+1 문제 때문
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages() {

        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }

}
