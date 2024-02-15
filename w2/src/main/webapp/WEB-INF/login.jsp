<%--
  Created by IntelliJ IDEA.
  User: 804-04
  Date: 2024-01-16
  Time: 오후 6:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>login.jsp</title>
</head>
<body>

<c:if test="${param.result == 'error'}">
    <h1>로그인 에러</h1>
</c:if>
    <form action="/login" method="post">
        <input type="text" name="mid">
        <input type="text" name="mpw">
        자동로그인 <input type="checkbox" name="auto">
        <button type="submit">LOGIN</button>
    </form>
</body>
</html>
