package org.zerock.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.sgr20240208.dao.TodoDAO;
import org.zerock.sgr20240208.domain.TodoVO;

public class TodoDAOTests {

    private TodoDAO todoDAO;

    @BeforeEach
    public void ready() {
        todoDAO = new TodoDAO();
    }


    @Test
    public void testTime() throws Exception {

        System.out.println(todoDAO.getTime());
    }

    @Test
    public void testInsert() throws Exception {

        TodoVO todoVO = TodoVO.builder()
                .name("test")
                .id("test")
                .password("test")
                .age(12)
                .gender("test")
                .hobbies("test")
                .travel("test")
                .content("test")
                .build();

        todoDAO.insert(todoVO);
    }

}
