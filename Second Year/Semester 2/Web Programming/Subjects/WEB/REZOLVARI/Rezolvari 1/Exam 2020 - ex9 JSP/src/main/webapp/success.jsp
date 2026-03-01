<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="products.model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="products.service.ProductService" %>
<%@ page import="static java.lang.System.out" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="../../../../../../../github/retake2022/src/main/webapp/Resources/jquery-2.0.3.js"></script>
</head>
<body>

    <%
    String username = (String) session.getAttribute("Username");
        List<Product> products = ProductService.getAll();
//out.print(products);
    %>

<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand"> Welcome <%=username%>! This is your shopping app!</a>
</nav>

    <div class="d-flex align-items-center justify-content-center">
        <form  class="form-group" action="ProductController" method="post">
            <div class="form-group">
                <label for="name">Name</label>
                <input type="text" name="name" class="form-control" id="name" aria-describedby="emailHelp" placeholder="Enter name ..." required>

                <label for="description">Description</label>
                <input type="text" name="description" class="form-control" id="description" aria-describedby="emailHelp" placeholder="Enter description ..." required>
            </div>

            <button type="submit" class="btn btn-primary"  name = "add">Add product</button>
        </form>
    </div>

    <div class="d-flex align-items-center justify-content-center" >
        <form  class="form-group" action="ProductController" method="get">
            <div class="form-group">
                <label for="filter">Filter</label>
                <input type="text" name="filter" class="form-control" id="filter" aria-describedby="emailHelp" placeholder="Enter filter" required>
            </div>

            <button  type="submit" class="btn btn-primary" name = "filterAction">Filter</button>
        </form>

    <ul class = "block-list justify-content-center" id = "mylist">
    <%
        if(request.getAttribute("products") != null){
            //out.print("yo");
            //out.print(request.getAttribute("products"));
            ArrayList<Product> std =
                    (ArrayList<Product>)request.getAttribute("products");

            if(!std.isEmpty())
                for(Product p:std) {
                    out.print("<li class = 'block-list' id = 'mylist'>" + p.getName() + "</li>");
                }
        }
        else
            out.print("no");

%>
    </ul>
    </div>
    <div class="d-flex align-items-center justify-content-center" >
    <label>
        <form  class="form-group" action="ProductController" method="post">
        <select name="category">
            <%
                products = ProductService.getAll();
            %>
            <c:forEach items="<%=products%>" var="category">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </select>
            <br>
        <label for="quantity">Quantity: </label>
        <input type="text" name="quantity" class="form-control" id="quantity" aria-describedby="emailHelp" placeholder="Enter quantity ..." required>

            <input type="submit" value="Submit" name = "dropdownAction">
        </form>
    </label>
    </div>

    <div class="d-flex align-items-center justify-content-center" >
    <form class="form-group" action="/ProductController" method="post">
     <input type="submit" value="Finalize Command" name = "finalize">
     <input type="submit" value="Cancel Command" name = "cancel">
    </form>
    </div>

</body>
</html>
