<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login</title>
</head>
<body>

Family Relations:
<ul class="familyRelations">
</ul>


<p>
Add new family relation:
<br>
Username:
<input type="text" id="username">
<br>
Mother:
<input type="text" id="mother">
<br>
Father:
<input type="text" id="father">
<br>
<button onclick="addFamilyRelation()">Add</button>
</p>

<p>
    Find you mother's descending family line:
    <br>
    <button onclick="findMotherFamilyLine()">Find out</button>
    <ul class="motherFamilyLine">
    </ul>
</p>
<p>
    Find you father's descending family line:
    <br>
    <button onclick="findFatherFamilyLine()">Find out</button>
    <ul class="fatherFamilyLine">
    </ul>
</p>

</body>
</html>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>



    function populate(data){
        console.log(data);

        var familyRelations = "";
        console.log(data);
        for(let familyRelation of data) {


            familyRelations += "<li>";

            familyRelations += familyRelation["username"] + " " + familyRelation["mother"] + " " + familyRelation["father"] + "</li>";
        }
        $(".familyRelations").html(familyRelations);
    }

    $(document).ready(function(){
        $.get("familyRelations", data => populate(data));
    });

    function addFamilyRelation(){
        var username = $("#username").val();
        var mother = $("#mother").val();
        var father = $("#father").val();

        $.post("familyRelations", {"username": username, "mother": mother, "father": father}, data => populate(data));
    }

    function findMotherFamilyLine(){
        $.get("motherFamilyLine",{},data => populateMotherFamilyLine(data));
    }

    function populateMotherFamilyLine(data){
        console.log(data);

        var motherFamilyLine = "";


        motherFamilyLine += "<li>";

        motherFamilyLine += data + "</li>";

        $(".motherFamilyLine").html(motherFamilyLine);
    }

    function findFatherFamilyLine(){
        $.get("fatherFamilyLine",{},data => populateFatherFamilyLine(data));
    }

    function populateFatherFamilyLine(data){
        console.log(data);

        var fatherFamilyLine = "";


        fatherFamilyLine += "<li>";

        fatherFamilyLine += data + "</li>";

        $(".fatherFamilyLine").html(fatherFamilyLine);
    }


</script>