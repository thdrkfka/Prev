package org.zerock.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.sgr20240208.dto.TodoDTO;
import org.zerock.sgr20240208.service.TodoService;

public class TodoServiceTests {

    private TodoService todoService;

    @BeforeEach
    public void ready() {
        todoService = TodoService.INSTANCE;
    }

    @Test
    public void testRegister() throws Exception {

        TodoDTO todoDTO = TodoDTO.builder()
                .name("test1")
                .id("test1")
                .password("test1")
                .age(12)
                .gender("test1")
                .hobbies("test1")
                .travel("test1")
                .content("test1")
                .build();

        todoService.register(todoDTO);
    }
}
