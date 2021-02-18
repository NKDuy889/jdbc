<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2/17/2021
  Time: 11:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>List customer</h1>
<form action="/customer">
    <input value="search" name="action" hidden>
    <input type="text" name="name" placeholder="nhập tên" >
    <input type="submit" value="tìm kiếm">
</form>
<table>
    <tr>
        <td>name</td>
        <td>age</td>
        <td>address</td>
    </tr>
    <c:forEach items="${list}" var="customerList">
        <tr>
            <td>${customerList.getName()}</td>
            <td>${customerList.getAge()}</td>
            <td>${customerList.getAddress()}</td>
            <td><a href="/customer?action=edit&id=${customerList.getId()}">edit</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
