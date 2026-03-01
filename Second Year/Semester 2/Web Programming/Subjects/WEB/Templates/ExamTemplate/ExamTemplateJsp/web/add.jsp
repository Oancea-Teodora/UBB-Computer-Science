
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add page</title>
    <link rel="stylesheet" href="stylesheet.css">

    <script>
        function validateForm() {
            var x = document.getElementById("grade").value;
            if (isNaN(x)) {
                alert("Grade must be a number");
                return false;
            }
            if (x < 1 || x > 10) {
                alert("Grade must be in 1-10 interval");
                return false;
            }
        }
    </script>
</head>
<body>

<h1>Add a new student</h1>

<form name="addFrom" action="AddServlet" method="get" onsubmit="return validateForm()">
    <table class="update">
        <tr>
            <td class="right">Name:</td>
            <td><input type="text" name="name" value="" required=""/></td>
        </tr>
        <tr>
            <td class="right">Grade:</td>
            <td><input type="text" name="grade" value="" required="" id="grade"/></td>
        </tr>
    </table>
    <br>
    <input type="submit" name="submit" value="Add">
</form>

</body>
</html>
