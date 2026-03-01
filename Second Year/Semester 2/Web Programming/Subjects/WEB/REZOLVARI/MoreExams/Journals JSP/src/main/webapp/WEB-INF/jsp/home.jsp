<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login</title>
</head>
<body>

<ul class="articles">
</ul>

Journal Name:
<input type="text" id="name">
<button onclick="searchJournals()">Search</button>
<br>

Add new article:
<br>
Journal Name:
<input type="text" id="journalName">
<br>
Summary:
<input type="text" id="summary">
<button onclick="addArticle()">Add</button>

</body>
</html>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

    var localList = []

    function populate(data){
        console.log(data);

        var articles = "";
        console.log(data);
        for(let article of data) {


            articles += "<li>";

            articles += article["summary"] + " " + article["date"] + "</li>";
        }
        $(".articles").html(articles);
    }

    function searchJournals(){
        var name = $("#name").val();

        $.get("journals/" + name, data => populate(data));
    }

    function addArticle(){
        var journalName = $("#journalName").val();
        var summary = $("#summary").val();

        $.post("addArticle", {"journalName": journalName, "summary": summary}, data => populate(data));
    }

    function checkNew() {
        $.get("new", function(data, status){
            console.log("Data: " + data + "\nStatus: " + status);
            if (data === true)
                alert("Someone added a new article!");
        });
    }

    setInterval(checkNew, 3000);

</script>