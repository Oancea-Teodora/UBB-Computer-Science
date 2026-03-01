<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Jsp Exam</title>
    <link rel="stylesheet" href="styles/main.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="scripts/main.js"></script>
</head>
<body>
<h1><%= "Jsp Exam" %></h1>
<hr>

<div>
    <label>
        Key: <input type="text" id="key" name="key"><br>
    </label>
    <label>
        Value: <input type="text" id="value" name="value"><br>
    </label>
    <button onclick="onAddKeywordBtn()">Add keyword</button><br>
</div>
<hr>

<div>
    <label>
        Title: <input type="text" id="title" name="title"><br>
    </label>
    <button onclick="onSearchBtn()">Search documents</button><br>

    <br>

    <table id="docs-table">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>List of Templates</th>
        </tr>
    </table>
</div>
<hr>

<label>
    Rendered content: <input style="width: 100%; height: 10%" type="text" id="render" name="render" readonly><br>
</label>

</body>
</html>
