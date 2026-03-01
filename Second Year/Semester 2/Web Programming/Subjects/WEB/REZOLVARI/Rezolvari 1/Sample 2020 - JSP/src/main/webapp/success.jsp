<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.model.Asset" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="jquery-2.0.3.js"></script>
    <script src="random.js"></script>
</head>
<body>

    <%
    Integer id = (Integer) session.getAttribute("Id");
    String username = (String) session.getAttribute("Username");
    List<Asset> assetList = new ArrayList<>();

    //out.println("HEY USER Name " + session.getAttribute("Username") + "ID: " + session.getAttribute("Id"));
    String spageid=request.getParameter("page");

    if(id == null){
        response.sendRedirect("index.jsp");
        return;
    }
    %>
<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand"> Welcome <%=username%>! This is your assets app!</a>

    <%-- Logout --%>
    <form class="form-inline" action="Logout" method="post">
        <button class="btn btn-outline-secondary my-2 my-sm-0" type="submit" name="logout" value="Logout">Logout</button>
    </form>
</nav>

    <div>
        <table id = "table-container" class = "table"></table>
    </div>

    <div id='addAsset' class="justify-content-center" style="height: 600px">
        <label>Name:  <input type="text" name="uname" class="form-control"></label><br>
        <br>
        <label>Description:  <input type="text" name="descr" class="form-control" ></label><br>
        <br>
        <label>Value:  <input type="text" name="value" class="form-control" ></label><br>
        <br>
        <button id = "addAssetButton">Add asset</button>
        <button id = "addAssetsButton">Add Assets</button>


    </div>


<script>
    let assetList = [];
    var userId = "<%=id%>";

    $(document).ready(

        function (){
           getAssets();

            $(document).on('click','#addAssetButton',function(e){
                e.preventDefault();
                // console.log("hey");
                // console.log($("input[name=uname]").val());
                // console.log($("input[name=descr]").val());
                // console.log($("input[name=value]").val());
                var obj = new Object();
                obj.name = $("input[name=uname]").val()
                obj.description  = $("input[name=descr]").val()
                obj.value = $("input[name=value]").val()
                var jsonString= JSON.stringify(obj);
                console.log(jsonString)
                assetList.push(jsonString)
                console.log(assetList)
            });

            $(document).on('click','#addAssetsButton',function(e){
                e.preventDefault();
                addAssets();
            });

        }
    );
</script>


</body>
</html>