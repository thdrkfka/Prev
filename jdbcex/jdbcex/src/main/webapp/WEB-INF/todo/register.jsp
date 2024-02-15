<%--
  Created by IntelliJ IDEA.
  User: 804-04
  Date: 2024-01-16
  Time: 오후 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>register.jsp</title>
</head>
<body>
<form action="/todo/register" method="post">
    <div>
        <input type="text" name="title" placeholder="INSERT TITLE">
    </div>
    <div>
        <input type="date" name="dueDate">
    </div>
    <div>
        <button type="reset">RESET</button>
        <button type="submit">REGISTER</button>
    </div>
</form>
</body>
</html>
