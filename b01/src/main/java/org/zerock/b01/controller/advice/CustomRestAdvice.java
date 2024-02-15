package org.zerock.b01.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {//예외에 대한 경고메세지 보이게끔!!!

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {

        log.error(e);

        Map<String, String> erroerMap = new HashMap<>();

        if(e.hasErrors()){
            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                erroerMap.put(fieldError.getField(), fieldError.getCode());
            });

        }

        return ResponseEntity.badRequest().body(erroerMap);

    }

    //클라이언트에 서버의 문제가 아니라 "데이터의 문제가 있음"을 알리기 위함.
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleFKException(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg", "constraint fails");

        return ResponseEntity.badRequest().body(errorMap);
    }

    //NoSuchElementException.class => 특정 댓글을 조회했을 때, 데이터가 존재하지 않을 때(reply 테이블의 rno가 존재하지 않은 번호일 때 에러 예외처리 추가)
    //EmptyResultDataAccessException.class => 특정 댓글을 삭제했을 때, 존재하지 않는 rno 번호의 삭제 예외
    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleNoSuchElement(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg", "No Such Element Exception");

        return ResponseEntity.badRequest().body(errorMap);
    }

}
