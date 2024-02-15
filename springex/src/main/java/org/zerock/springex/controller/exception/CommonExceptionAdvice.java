package org.zerock.springex.controller.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;

@Log4j2
@ControllerAdvice
public class CommonExceptionAdvice { //스프링 MVC 예외 처리

    ///ex7?p1=AAA&p2=BBB int 타입에 숫자 대신 알파벳 보내면 예외처리 하게끔 함.
    @ResponseBody //웹 브라우저에 그대로 전송
    @ExceptionHandler(NumberFormatException.class)
    public String exceptNumber (NumberFormatException numberFormatException) {

        log.error("-----------------------------------");
        log.error(numberFormatException.getMessage());

        return "NUMBER FORMAT EXCEPTION"; //=> 웹에 뜨게 함.
    }

    //오류의 원인을 자세히 나열해서 웹에서 보여줌.
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public String exceptNumber (Exception exception) {

        log.error("-----------------------------------");
        log.error(exception.getMessage());

        StringBuffer buffer = new StringBuffer("<ul>");

        buffer.append("<li>" + exception.getMessage() + "</li>");

        Arrays.stream(exception.getStackTrace()).forEach(stackTraceElement -> {
            buffer.append("<li>" + stackTraceElement + "</li>");
        });

        buffer.append("</ul>");

        return buffer.toString();
    }

    //아예 존재하지 않는 터무니 없는 경로일 때 Exception
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(){
        return "custom404";
    }
}
