<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login</title>
</head>
<body>

<ul class="assets">
</ul>

Name:
<input type="text" id="name">
Description:
<input type="text" id="description">
Value:
<input type="text" id="value">
<button onclick="addAsset()">Add</button>
<button onclick="submit()">Submit</button>

</body>
</html>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

    function populate(data){
        console.log(data);

        var assets = "";
        console.log(data);
        for(let asset of data) {
            if (parseInt(asset.value) > 10) {
                assets += "<li style = 'background-color:red'>";
            }
            else {
                assets += "<li>";
            }
            assets += asset.name + " " + asset["description"] + " " + asset["value"] + "</li>";
        }
        $(".assets").html(assets);
    }

    $(document).ready(function(){
        $.get("assets", data => populate(data));
    });

    var toBeAdded = [];
    function addAsset() {
        var name = $("#name").val();
        var description = $("#description").val();
        var value = $("#value").val();

        var asset = { name, description, value };
        toBeAdded.push(asset);

        var assets = $(".assets").html();
        if (parseInt(value) > 10) {
            assets += "<li style = 'background-color:red'>";
        }
        else {
            assets += "<li>";
        }
        assets += name + " " + description + " " + value + "</li>";
        $(".assets").html(assets);
        console.log(JSON.stringify(toBeAdded));
    }


    function submit() {
        var toSend = JSON.stringify(toBeAdded);
        console.log(toSend);
        $.ajax({
            url: 'assets',
            type: 'post',
            data: toSend,
            contentType: "application/json; charset=utf-8",
            success: function (data){
                populate(data);
                toBeAdded = [];
            }
        });
    }
</script>