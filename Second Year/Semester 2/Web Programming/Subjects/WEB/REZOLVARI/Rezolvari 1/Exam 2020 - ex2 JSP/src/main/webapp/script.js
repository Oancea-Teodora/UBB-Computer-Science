
$(document).on('click','#view',function(e){
    e.preventDefault();

    const types = $("[name='user']")[0].value;
    console.log(types)
    console.log("hello?")
    $.ajax({
        type: "POST",
        url: "Channel",
        datatype: "json",
        data: { action: "getChannels", owner: types },
        success: function(data){
            //$("#table-container").html(data);
            console.log(data)
            showData(data);
        },
        error: function (error) {
            alert(error.toString());
        }
    });
});

function showData(data){
    let myTable ="<table> <tr> <th>Id</th> <th>OwnerId</th> <th>Name</th> <th>Description</th> <th>Subscribers</th> </tr>"

    //var myRecipes = JSON.encode(data);
    //console.log(myRecipes)
    //
    // for(let recipe of myRecipes)

    // let tableData = "";

    for(let r of data){
        console.log("recipe = ", r)
        myTable += "<tr>" +
            "<td>"+ r.id + "</td>" +
            "<td>"+ r.owner + "</td>" +
            "<td>"+ r.name + "</td>" +
            "<td>"+ r.description + "</td>" +
            "<td>"+ r.subscribers + "</td>" +

            "</tr>"
    }

    myTable += "</table>";
    //console.log(myTable);
    //document.getElementById("table-container").appendChild(myTable);
    //$(myTable).appendTo("#table-container");
    $("#table-container").html(myTable)
}