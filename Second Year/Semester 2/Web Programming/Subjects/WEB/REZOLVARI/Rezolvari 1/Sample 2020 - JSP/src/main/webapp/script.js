function getAssets(){
    $.getJSON("Asset", {action: "getAssets"}, function (res){
        showData(res)
        }
    );
}


function showData(data){
    console.log("res: " + res);
    let myTable ="<table> <tr> <th>Id</th> <th>UserID</th> <th>Name</th> <th>Summary</th> <th>Value</th> </tr>"

    for(let article of data){
        console.log("recipe = ", article)
        myTable += "<tr>" +
            "<td>"+ article.id + "</td>" +
            "<td>"+ article.user + "</td>" +
            "<td>"+ article.name + "</td>" +
            "<td>"+ article.description + "</td>" +
            "<td>"+ article.value + "</td>" +

            "</tr>"
    }

    myTable += "</table>";
    //console.log(myTable);
    //document.getElementById("table-container").appendChild(myTable);
    //$(myTable).appendTo("#table-container");
    $("#table-container").html(myTable)
}
