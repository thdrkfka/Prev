package org.zerock.sgr20240208.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.sgr20240208.dao.TodoDAO;
import org.zerock.sgr20240208.domain.TodoVO;
import org.zerock.sgr20240208.dto.TodoDTO;
import org.zerock.sgr20240208.util.MapperUtil;

@Log4j2
public enum TodoService {

    INSTANCE;

    private TodoDAO dao;
    private ModelMapper modelMapper;

    TodoService() {
        dao = new TodoDAO();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public void register(TodoDTO todoDTO) throws Exception {

        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);

        log.info(todoVO);

        dao.insert(todoVO);
    }

}
