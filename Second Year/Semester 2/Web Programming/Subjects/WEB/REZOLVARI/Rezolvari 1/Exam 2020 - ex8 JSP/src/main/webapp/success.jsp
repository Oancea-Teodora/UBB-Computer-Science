<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="jquery-2.0.3.js"></script>
    <script src="script.js"></script>
</head>
<body>

<%

    String username = (String) session.getAttribute("Username");
    String mother = (String) session.getAttribute("mother");
    String father = (String) session.getAttribute("father");
%>

<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand"> Welcome <%=username%>! This is your family tree app!</a>
</nav>

</div>
<div class="d-flex align-items-center justify-content-center" >
    <label>
        <form  class="form-group" action="FamilyController" method="post">
            <select name="family">
                    <option value="<%=mother%>"><%=mother%></option>
                    <option value="<%=father%>"><%=father%></option>
            </select>
            <br>
            <label for="mother">Mother name:  </label>
            <input type="text" name="mother" class="form-control" id="mother" aria-describedby="emailHelp" placeholder="Enter mother name ..." >
            <br>
            <label for="father">Father name:  </label>
            <input type="text" name="father" class="form-control" id="father" aria-describedby="emailHelp" placeholder="Enter father name ..." >

            <input type="submit" value="Submit" name = "dropdownAction">
        </form>
    </label>
</div>
<div>
    <table id = "table-container"></table>
</div>
<button id = "showFathers">Get Fathers</button>
<script>
    var child = "<%=username%>"

    $(document).ready(
        function (){
            // $("#showFathers").click(
            //     function (){
            //         getFathers(function (route){
            //             console.log(route)
            //         });
            //
            //
            //     }
            // );
        }
    )
</script>


</body>
</html>