<%--
  Created by IntelliJ IDEA.
  User: 804-04
  Date: 2024-02-08
  Time: 오전 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>회원 가입 목록</h2>
<p>이름 : ${param.name}</p>
<p>아이디 : ${param.id}</p>
<p>비밀번호 : ${param.password}</p>
<p>나이 : ${param.age}</p>
<p>성별 : <c:if test="${param.gender == true}">남</c:if>
         <c:if test="${param.gender != true}">여</c:if></p>
<p>취미 : ${param.hobbies}</p>
<p>가고 싶은 여행지 : ${param.travel}</p>
<p>비고란 : ${param.content}</p>


</body>
</html>
