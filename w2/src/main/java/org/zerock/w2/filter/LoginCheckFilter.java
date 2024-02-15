package org.zerock.w2.filter;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import javax.servlet.annotation.WebFilter;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
@WebFilter(urlPatterns = {"/todo/*"})
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Login check filter....");

        HttpServletRequest req = (HttpServletRequest) request; //Get
        HttpServletResponse resp = (HttpServletResponse) response; //Post

        HttpSession session = req.getSession();

        if (session.getAttribute("loginInfo") != null) {
            chain.doFilter(request, response);
            return;
        }

        //session에 loginInfo 값이 없다면
        //쿠키를 체크하라
        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        //세션에도 없고 쿠키도 없다면 그냥 로그인으로
        if(cookie == null) {
            resp.sendRedirect("/login");
            return;
        }

        //쿠키가 존재하는 상황이라면
        log.info("cookie는 존재하는 상황");

        //uuid값
        String uuid = cookie.getValue();

        try {
            //데이터베이스 확인
            MemberDTO memberDTO = MemberService.INSTANCE.getByUUID(uuid);

            log.info("쿠키의 값으로 조회한 사용자 정보 : " + memberDTO);

            if (memberDTO == null) {
                throw new Exception("Cookie value is not valid");
            }

            //회원 정보를 세션에 추가
            session.setAttribute("loginInfo", memberDTO);
            chain.doFilter(request,response);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/login");
        }
//        if(session.getAttribute("loginInfo") == null) {
//            resp.sendRedirect("/login");
//            return; //return에 아무것도 없으면 -> 종료
//        }

//        chain.doFilter(request,response);
    }

    private Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        Optional<Cookie> result = Arrays.stream(cookies)
                .filter(ck -> ck.getName().equals(name))
                .findFirst();

        return result.isPresent()?result.get():null;
    }
}
