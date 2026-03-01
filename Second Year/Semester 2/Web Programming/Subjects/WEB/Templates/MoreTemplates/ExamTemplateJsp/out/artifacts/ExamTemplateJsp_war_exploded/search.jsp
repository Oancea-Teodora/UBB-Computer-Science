
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View filtered students</title>
    <link rel="stylesheet" href="stylesheet.css">
</head>
<body>
<h1>Search students</h1>
<form name="searchForm" action="SearchController" method="get">
    <label>Enter name:</label>
    <input type="text" name="searchValue" value="" />
    <br><br>
    <input type="submit" name="submit" value="Search">
</form>

</body>
</html>
