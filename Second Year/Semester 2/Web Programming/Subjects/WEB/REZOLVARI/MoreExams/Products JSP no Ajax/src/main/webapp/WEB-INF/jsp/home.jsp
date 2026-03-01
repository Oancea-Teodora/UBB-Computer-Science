<%@ page language="java" contentType="text/html; ISO-8859-1" pageEncoding="ISO-8859-1" %>

<html>
<head>
    <title>Login</title>
</head>
<body>

<br>
<form action="products" method="post">
    Specify a name:
    <input type="text" name="name">
    <br>
    Specify a description:
    <input type="text" name="description">
    <br>
    <button type="submit">Add</button>
</form>

<form action="search" method="get">
    Specify a name:
    <input type="text" name="name">
    <button type="submit">Search</button>
</form>

<form action="command" method="post">
    Specify a name:
    <input type="text" name="name">
    <button type="submit">Search</button>
</form>

<form action="addItem" method="post">
    Specify a name:
    <input type="text" name="productName">
    <br>
    Specify a quantity:
    <input type="text" name="quantity">
    <br>
    <button type="submit">Add</button>
</form>

<form action="confirm" method="get">
    <button type="submit">Place Order</button>
</form>

<form action="cancel" method="get">
    <input type="text" name="x">
    <button type="submit">Cancel Order</button>
</form>


</body>
</html>