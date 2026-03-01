function getArticles(filter, res){
    $.getJSON("website", {action: "getDocuments", filter: filter}, res
    );
}
