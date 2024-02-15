package org.zerock.sgr20240208.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {

    private Long tno;

    private String name;

    private String id;

    private String password;

    private int age;

    private String gender;

    private String hobbies;

    private String travel;

    private String content;

}
