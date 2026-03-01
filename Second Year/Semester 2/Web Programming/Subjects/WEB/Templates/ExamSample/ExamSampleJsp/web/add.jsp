
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String table = (String) request.getAttribute("table");
    int id = (int) request.getAttribute("id");
%>
<html>
<head>
    <title>Add page</title>
    <link rel="stylesheet" href="stylesheet.css">

    <style>
        #generate {
            color: white;
            background-color: red;
        }
    </style>

    <script>
        function generateFields() {
            var id_input = document.createElement("INPUT");
            id_input.setAttribute("type", "hidden");
            id_input.setAttribute("name", "id");
            id_input.setAttribute("value", "<%=id%>");
            id_input.setAttribute("class", "id");
            document.getElementById("form").appendChild(id_input);

            var name_input = document.createElement("INPUT");
            name_input.setAttribute("type", "text");
            name_input.setAttribute("name", "name");
            name_input.setAttribute("placeholder", "Enter name");
            name_input.setAttribute("value", "");
            name_input.setAttribute("class", "name");
            document.getElementById("form").appendChild(name_input);

            var description_input = document.createElement("INPUT");
            description_input.setAttribute("type", "text");
            description_input.setAttribute("name", "description");
            description_input.setAttribute("placeholder", "Enter description");
            description_input.setAttribute("value", "");
            description_input.setAttribute("class", "description");
            document.getElementById("form").appendChild(description_input);

            var value_input = document.createElement("INPUT");
            value_input.setAttribute("type", "text");
            value_input.setAttribute("name", "value");
            value_input.setAttribute("placeholder", "Enter value");
            value_input.setAttribute("value", "");
            value_input.setAttribute("class", "value");
            document.getElementById("form").appendChild(value_input);

            var br = document.createElement("br");
            var form = document.getElementById("form");
            form.appendChild(br);
        }
    </script>
</head>
<body>
<h1>Add a new assets. UserId=<%=id%></h1>
<button onclick="generateFields()" id="generate">+</button><br>

<br><br>
<form action="" method="POST" class="form-submit" id="form">
    <input type="hidden" name="id" value="<%=id%>" class="id">
    <label>Name</label>
    <input type="text" name="name" placeholder="Enter name" class="name" value=""><br>
    <label>Description</label>
    <input type="text" name="description" placeholder="Enter description" value="" class="description" ><br>
    <label>Value</label>
    <input type="text" name="value" placeholder="Enter value"  value="" class="value" ><br><br>

    <!--This form has initially "one" place for adding objects, but + button is able to generate more inputs-->
    <button type="submit" name="save" class="add" >Save</button><br><br>
</form>

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<!-- Popper JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
    $(document).ready(function(){
        $(".add").click(function(e){
            e.preventDefault();
            //get all the values from the form
            var addId = $('.id').map((_,el) => el.value).get();
            var addName = $('.name').map((_,el) => el.value).get();
            var addDescription = $('.description').map((_,el) => el.value).get();
            var addValue = $('.value').map((_,el) => el.value).get();
            var array = [];

            for (var i = 0; i < addId.length; i++) {
                var asset = {id: addId[i], name: addName[i], description: addDescription[i], value: addValue[i]};
                var myJSON = JSON.stringify(asset);
                array.push(myJSON);
            }

            var json = JSON.stringify(array);

            $.ajax({
                url: "${pageContext.request.contextPath}/AddServlet",
                method: 'post',
                data: {json: json },
                success: function(response){
                    window.scrollTo(0,0);
                    alert("Added successfully");
                    window.location.replace("/ReadController?userId=<%=id%>");
                }
            });
        });
    });
</script>

</body>
</html>
