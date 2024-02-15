package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("log out..................");

        HttpSession session = req.getSession();

        session.removeAttribute("loginInfo");
        //session 지우려고
        // => 이유:session을 남기지 않아야 다음에 로그인 할 때 안남고 재로그인 시킬 수 있음.
        session.invalidate(); //session 없는 거 입증

        resp.sendRedirect("/login");
    }
}
