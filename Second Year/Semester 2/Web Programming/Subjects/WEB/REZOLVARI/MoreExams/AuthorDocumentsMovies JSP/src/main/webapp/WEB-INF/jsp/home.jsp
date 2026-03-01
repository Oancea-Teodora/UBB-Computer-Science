<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>



<p>
    Add a document:
    <br>
    Name:
    <input type="text" id="name">
    <br>
    Contents:
    <input type="text" id="contents">
    <br>
    <button onclick="addDocument()">Add</button>
</p>

<ul class="interleave1">

</ul>
<ul class="interleave2">

</ul>
<button onclick="displayInterleaved()">Interleave</button>

Display documents:
<ul class="documents">
</ul>

Display the Document with the largest number of authors:
<button onclick="mostFrequentDocument()">Most Frequent</button>

</body>
</html>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

    function populate(data){
        console.log(data);

        var documents = "";
        console.log(data);
        for(let document of data) {


            documents += "<li>";
            documents += document["name"] + " " + document["contents"] ;
            documents += "</li>";
        }
        $(".documents").html(documents);
    }

    function populateFirstList(data){
        console.log(data);

        var documents = "";
        console.log(data);
        for(let document of data) {


            documents += "<li>";
            documents += document["name"] + " " + document["contents"] ;
            documents += "</li>";
        }
        $(".interleave1").html(documents);
    }

    function populateSecondList(data){
        console.log(data);

        var movies = "";
        console.log(data);
        for(let movie of data) {


            movies += "<li>";
            movies += movie["title"] + " " + movie["duration"] ;
            movies += "</li>";
        }
        $(".interleave2").html(movies);

    }


    $(document).ready(function () {
        $.get("document", data => populate(data));
    });



    function addDocument(){
        var name = $("#name").val();
        var contents = $("#contents").val();

        $.post("document",{"name": name, "contents": contents}, data => populate(data));
    }

    async function displayInterleaved() {
        await $.get("interleave1", data => populateFirstList(data));
        await $.get("interleave2", data => populateSecondList(data));
        interleave();
    }

    function interleave(){
        var $list = $('ul:eq(1)').detach().children();

        $('ul:eq(0) li').after(function (i) {
            return $list.eq(i);
        });

    }

    function mostFrequentDocument(){
        $.get("mostfrequent", data => populate(data));
    }




</script>