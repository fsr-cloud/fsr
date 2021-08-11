<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>用户注册</h1>
<c:if test="${ not empty msg}">${msg}</c:if>
<form action="${pageContext.request.contextPath}/user/register" method="post">
    用户名:<input type="text" name="username" > <br/>
    密码  : <input type="text" name="password"> <br>
    <input type="submit" value="注册">
</form>
</body>
</html>
