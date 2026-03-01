
function renderDocument(documentId){
    console.log("hello?")
    $.ajax({
        type: "GET",
        url: "website",
        datatype: "json",
        data: { action: "renderDocument", docid: documentId },
        success: function(data){
            //$("#table-container").html(data);
            console.log(data)
            showData(data);
        },
        error: function (error) {
            alert(error.toString());
        }
    });
};

function showData(data){
    console.log("hello")
    $.ajax({
        type: "GET",
        url: "website",
        datatype: "json",
        data: {action: "getKeywords"},
        success: function(res){
            console.log("keywords: " + res);
            $("#render").html("<ul class = \"list\"></ul>")
            for(let template of data){
                for(let keyword of res){
                    let key = keyword.key;
                    let value = keyword.value;
                    console.log("key : " + key + " ; value: " + value)
                    template['text'] = template['text'].replace(`{{${key}}}`, value);
                }
                console.log(template)
                $("#render .list").append("<li>" + template.text + "</li>");

            }
        },
        error: function (error) {
            console.log(error);
        }
    })
}