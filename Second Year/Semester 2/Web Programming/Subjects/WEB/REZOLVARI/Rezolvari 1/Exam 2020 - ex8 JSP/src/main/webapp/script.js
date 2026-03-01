// function getFiles(callbackFunction){
//     $.getJSON("FileController", {action: "getAll", userId: sessionUserId},
//         callbackFunction);
// }

function getFathers(callbackFunction){
    $.getJSON("FamilyController", {action: "getFathers", child: child}, callbackFunction)
}

 $(document).on('click','#showFathers',function(e){
     console.log("hello?")
     $.ajax({
         type: "GET",
         url: "FamilyController",
         datatype: "json",
         data: { action: "getFathers", child: child },
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
    let myTable ="<table> <tr> <th>Father</th> </tr>"

    //var myRecipes = JSON.encode(data);
    //console.log(myRecipes)
    //
    // for(let recipe of myRecipes)

    // let tableData = "";

    for(let father of data){
        console.log("recipe = ",father)
        myTable += "<tr>" +
            "<td>"+ father.father + "</td></tr>"
    }

    myTable += "</table>";
    console.log(myTable);
    //document.getElementById("table-container").appendChild(myTable);
    //$(myTable).appendTo("#table-container");
    $("#table-container").html(myTable)
}