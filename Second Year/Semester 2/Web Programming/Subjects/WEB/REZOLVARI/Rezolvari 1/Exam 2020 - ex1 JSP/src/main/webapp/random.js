function getArticles(res){
    console.log("hey>")
    $.getJSON("Journal", {action: "getArticles"}, res
         );
}


function setLast(res){
    this.lastArticle = res;
    console.log("lastt: " + this.lastArticle)
}


function notifyNewArticle(){
    let lastArticle;
    getArticles(function(res){
        //console.log("res: " + res);
        lastArticle = res[res.length-1];
        console.log("lasst: " + lastArticle.id);

        setInterval(
            () =>{
                getArticles(function(res) {
                    last = res[res.length-1];
                     console.log("last in memory: " + lastArticle.id + " " + lastArticle.user);
                     console.log("last >> : " + last.id + " " + last.user)
                     if(last != undefined && last.user!=username && lastArticle.id != last.id) {
                         alert("new article added by " + last.user +" : summary: " + last.summary);
                    //     //if (this.lastArticle != last)
                         lastArticle = last;
                     }
                     if(this.lastArticle == undefined)
                         this.lastArticle = last;
                })
            }, 5000
        )
    })


}