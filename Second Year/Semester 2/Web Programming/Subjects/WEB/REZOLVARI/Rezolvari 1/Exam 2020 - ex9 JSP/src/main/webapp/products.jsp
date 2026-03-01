<%@ page import="products.model.Product" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: amina
  Date: 6/11/2022
  Time: 5:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    ArrayList<Product> std =
            (ArrayList<Product>)request.getAttribute("products");
    if(!std.isEmpty())
        for(Product p:std) {
            out.print(p + "\n");
        }
%>
</body>
</html>
