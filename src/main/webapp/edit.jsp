<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2/17/2021
  Time: 11:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Edit Customers</h1>
<form method="post">

    <input type="text" name="name" value="${customer.getName()}"/>
    <input type="text" name="age" value="${customer.getAge()}"/>
    <input type="text" name="address" value="${customer.getAddress()}"/>
    <button type="submit">Edit</button>
</body>
</html>
