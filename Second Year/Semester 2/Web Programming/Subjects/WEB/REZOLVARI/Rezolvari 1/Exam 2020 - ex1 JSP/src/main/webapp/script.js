
$(document).on('click','#viewJournal',function(e){
    e.preventDefault();

    const types = $("[name='category']")[0].value;
    console.log(types)
    console.log("hello?")
    $.ajax({
        type: "POST",
        url: "Journal",
        datatype: "json",
        data: { action: "getJournals", category: types },
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

// function getArticles(){
//     console.log("hey>")
//     // $.getJSON("JournalController", {action: "getArticles"}, function (res
//     // ){
//     //     console.log("last: " + res);
//     // })
// }

// $(document).on('click','#showFathers',function(e)
// $(document).on('click', '#get', function(e){
//     e.preventDefault();
//     console.log("hello");
// });

function showData(data){
    let myTable ="<table> <tr> <th>Id</th> <th>User</th> <th>Journal Id</th> <th>Summary</th> <th>Date</th> </tr>"

    //var myRecipes = JSON.encode(data);
    //console.log(myRecipes)
    //
    // for(let recipe of myRecipes)

    // let tableData = "";

    for(let article of data){
        console.log("recipe = ", article)
        myTable += "<tr>" +
            "<td>"+ article.id + "</td>" +
            "<td>"+ article.user + "</td>" +
            "<td>"+ article.journalId + "</td>" +
            "<td>"+ article.summary + "</td>" +
            "<td>"+ article.date + "</td>" +

            "</tr>"
    }

    myTable += "</table>";
    console.log(myTable);
    //document.getElementById("table-container").appendChild(myTable);
    //$(myTable).appendTo("#table-container");
    $("#table-container").html(myTable)
}

function getLastArticle(){
    console.log("hey")
    return "hh";
}
//
// function notifyNewArticle(){
//     this.lastArticle = getArticles();
//     //console.log("het>");
//     setInterval(
//         () =>{
//             let last = getArticles();
//             console.log("last in memory: " + this.lastArticle);
//             console.log(last)
//             if(last != undefined && last.user!=username && this.lastArticle.id != last.id) {
//                 alert("new article added by " + last.user +" : summary: " + last.summary);
//                 //if (this.lastArticle != last)
//                 this.lastArticle = last;
//             }
//             if(this.lastArticle == undefined)
//                 this.lastArticle = last;
//         }, 5000
//     )
// }