package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {//페이징 관련 정보, 검색 조건, 키워드 추가 지정

    @Builder.Default
    private int page = 1;//기본으로 고정//request of()

    @Builder.Default
    private int size = 10;//기본으로 고정

    private String type; //검색의 종류 : t, c, w, tc, tw, wc, twc

    private String keyword;

    //현재 검색종류는 문자열로 받는데 배열로 반환해주는 기능
    public String[] getTypes(){

        if(type == null || type.isEmpty()){

            return null;
        }
        return type.split("");
    }

    //페이징 처리//Pageable 타입 반환
    public Pageable getPageable(String...props) {

        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    //검색 조건과 페이징 조건 등을 문자열로 구성//현재 검색종류는 문자열로 받는데 배열로 반환해주는 기능
    private String link;

    //웹 페이지 링크
    public String getLink() {

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);

            if(type != null && type.length() > 0){
                builder.append("&type="+type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
            link = builder.toString();
        }
        return link;
    }
}
