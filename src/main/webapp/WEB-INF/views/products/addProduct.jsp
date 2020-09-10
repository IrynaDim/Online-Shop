<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new product</title>
</head>
<body>
<h1>Hello! Please add new product.</h1>

<h4 style="color:red">${message}</h4>

<form method="post" action="${pageContext.request.contextPath}/products/addProduct">
    Please add type of a product: <input type="text" name="name">
    Please add price of a product: <input type="text" name="price">
    <button type="submit">Register</button>
</form>
</body>
</html>
