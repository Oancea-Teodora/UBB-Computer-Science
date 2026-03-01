
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mvcIntelliJIdea.model.Student"%>
<% Student student = (Student) request.getAttribute("student"); %>

<html>
<head>
    <title>Update page</title>
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

<h1>Update the student</h1>

<form name="updateFrom" action="UpdateServlet" method="get" onsubmit="return validateForm()">
    <table class="update">
        <tr>
            <td class="right">Id:</td>
            <td><input type="text" name="id" value="<%= student.getId() %>" readonly/></td>
        </tr>
        <tr>
            <td class="right">Name:</td>
            <td><input type="text" name="name" value="<%= student.getName() %>" required=""/></td>
        </tr>
        <tr>
            <td class="right">Grade:</td>
            <td><input type="text" name="grade" value="<%= student.getGrade() %>" required="" id="grade"/></td>
        </tr>
    </table>
    <br>
    <input type="submit" name="submit" value="Update">
</form>

</body>
</html>
