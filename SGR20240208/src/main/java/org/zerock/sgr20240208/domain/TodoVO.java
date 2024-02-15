package org.zerock.sgr20240208.domain;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TodoVO {

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
