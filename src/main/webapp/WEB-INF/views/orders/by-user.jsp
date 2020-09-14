<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ALl your orders</title>
</head>
<body>
<h1>All your orders</h1>
<table border="1">
    <tr>
        <th>Order id</th>
    </tr>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.orderId}"/>
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
