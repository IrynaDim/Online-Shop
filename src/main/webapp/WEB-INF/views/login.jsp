<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Login page</h1>
<h4 style="color:red">${message}</h4>

<form action="${pageContext.request.contextPath}/login" method="post" >
    Please provide your login: <input type="text" name="login">
    Please provide your password: <input type="password" name="pwd">
    <button type="submit">Login</button>
</form>
<a href="${pageContext.request.contextPath}/users/registration"> Registration. </a>
</body>
</html>

