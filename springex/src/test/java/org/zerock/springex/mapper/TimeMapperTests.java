package org.zerock.springex.mapper;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class TimeMapperTests {

    // mybatis의 @Select 어노테이션 이용해서 sql 쿼리문 작성하기 위함.
    @Autowired(required = false)
    private TimeMapper timeMapper;

    @Test
    public void testGetTime() {
        log.info(timeMapper.getTime());
    }

    // xml로 sql문 분리시켜서 작성
    @Autowired(required = false)
    private TimeMapper2 timeMapper2;

    @Test
    public void testNow() {
        log.info(timeMapper2.getNow());
    }
}
