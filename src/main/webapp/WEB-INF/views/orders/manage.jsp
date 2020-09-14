<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ALl orders</title>
</head>
<body>
<h1>All orders</h1>
<table border="1">
    <tr>
        <th>Order id</th>
        <th>User id</th>
    </tr>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.orderId}"/>
            </td>
            <td>
                <c:out value="${order.userId}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/orders/delete?id=${order.orderId}">Delete</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/orders/details?id=${order.orderId}">Details</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/"> Go to the main page </a>
</body>
</html>
