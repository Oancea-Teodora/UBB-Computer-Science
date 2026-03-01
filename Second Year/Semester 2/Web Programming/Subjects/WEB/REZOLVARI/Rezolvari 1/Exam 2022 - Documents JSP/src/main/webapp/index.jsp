<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="jquery-2.0.3.js"></script>
    <script src="random.js"></script>
    <script src="random2.js"></script>
</head>
<html>


<div class="d-flex align-items-center justify-content-center">
    <form  class="form-group" action="website" method="post">
        <div class="form-group">
            <label for="key">Keyword key</label>
            <input type="text" name="key" class="form-control" id="key" aria-describedby="emailHelp" placeholder="Enter article name ..." required>

            <label for="value">Keyword value</label>
            <input type="text" name="value" class="form-control" id="value" aria-describedby="emailHelp" placeholder="Enter description ..." required>

        </div>

        <button type="submit" class="btn btn-primary"  name = "add">Add Keyword</button>
    </form>
</div>
<br>
<br>
<div>
    <input type="text" name="ff"  id="filter"  placeholder="Enter filter ..." required>
    <button id = "filterTitle">Filter titles</button>
</div>
<div id = "doc">

</div>

<div id = "render">

</div>

</body>
</html>

<script>
    $(document).ready(

        function(){
            $(document).on('click', '#filterTitle', function(e){
                e.preventDefault();
                var filter = $("input[name=ff]").val();
                console.log("filter = " + filter);
                getArticles(filter, function(res){
                    console.log(res);
                    $("#doc").html("<ul id = 'l' class = \"list\"></ul>");
                    for(let document of res){
                        console.log("document: " + document);
                        $("#doc .list").append("<li>" + document.id + '.' + document.title + ' ;; ' + document.list + "</li>");
                    }
                    $("#doc .list li:even").css("background-color", "coral");

                    $("#doc .list").on("click", function (event){
                        console.log("hee");
                        var target = event.target;
                        //alert(event.target.innerHTML);
                        //console.log(event.target.innerHTML[0]);
                        renderDocument(event.target.innerHTML[0]);
                        // send request to controller using the printed thing
                    })
                })

            });

            // $("#dataTable tbody tr").on("click", function(event){
        //    console.log($(this).text());
        //});


            // IE does not know about the target attribute. It looks for srcElement
// This function will get the event target in a browser-compatible way
            function getEventTarget(e) {
                e = e || window.event;
                return e.target || e.srcElement;
            }

            var ul = document.getElementById('l');
            console.log("lsu: " + ul);
            ul.onclick = function(event) {
                var target = event.target;
                alert(event.target.innerHTML);
            };
        }
    )
</script>