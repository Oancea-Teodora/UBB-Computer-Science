function getAssets(){
    $.getJSON("Asset", {action: "getAssets"}, function (res){
        console.log("res: " + res);
            showData(res)
        }
    );
}


function showData(data){
    console.log("res: " + data);
    let myTable ="<table> <tr> <th>Id</th> <th>UserID</th> <th>Name</th> <th>Summary</th> <th>Value</th> </tr>"


    for (let article of data) {

        //console.log("recipe = ", article)
        myTable += "<tr>" +
            "<td>" + article.id + "</td>" +
            "<td>" + article.userid + "</td>" +
            "<td>" + article.name + "</td>" +
            "<td>" + article.description + "</td>" +
            "<td>" + article.value + "</td>" +

            "</tr>"

    }

    myTable += "</table>";
    //console.log(myTable);
    //document.getElementById("table-container").appendChild(myTable);
    //$(myTable).appendTo("#table-container");
    $("#table-container").html(myTable)

    $("tr").each(function() {
        $this = $(this);
        //console.log($this.html());
        //console.log($("td:nth-child(n)").text())

    });

    index = 2;
    for (let article of data) {

        //console.log("recipe = ", article)

        if (article.value >= 10) {
            //console.log("hey? " + index)
            //console.log("hey: " + $("table tr:nth-child(" + index + ")").html());
            $("table tr:nth-child(" + index + ")").css('background-color', 'red')
        }
        index += 1;
    }

}

function addAssets(){
    $.post("Asset", {action: "add", userId: userId, assets: assetList.toString()},
        function(){
            //console.log("USER ID: " + sessionUserId);
            console.log("SUCCESS");
        }
    );
}