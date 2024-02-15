package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    //메모리에 저장하기 위함. => 하드에 필드를 저장해야지 메모리에 저장할 수 있음.
    //필드 선언
    private Long bno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String writer;
    
    private LocalDateTime regDate;
    
    private LocalDateTime modDate;

    //첨부파일의 이름들
    private List<String> fileNames;

}
