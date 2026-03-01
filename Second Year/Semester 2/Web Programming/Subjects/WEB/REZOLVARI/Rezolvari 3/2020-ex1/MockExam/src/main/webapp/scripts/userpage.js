$(document).ready(() => {
    let last;

    $.get(
        "../home?action=checkLastArticle",
        {

        },
        (response) => {
            last = response;
            console.log(last);

            setInterval(function() {
                $.get(
                    "../home?action=checkLastArticle",
                    {

                    },
                    (response) => {
                        const newLast = response;
                        if (newLast !== last) {
                            alert(newLast);
                            last = newLast;
                        }
                    }
                )
            }, 5000);
        }
    )
})

function getAllArticlesFromJournalBtnClicked() {
    const journalName = document.getElementById("journal-name").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../home?action=getArticlesFromJournal&journal=" + journalName, true);
    getRequest.send();

    getRequest.onload = function() {
        const ulElem = document.getElementsByTagName("ul");
        if (ulElem.length > 0) {
            document.getElementsByTagName("body").item(0).removeChild(ulElem[0])
        }

        const articles = JSON.parse(this.responseText);

        let htmlList = '<ul>';

        articles.forEach(article => {
            const id = article["id"];
            const user = article["user"];
            const journalid = article["journalid"];
            const summary = article["summary"];
            const date = article["date"];

            htmlList += '<li>' + `Id(${id}),User(${user}),JournalId(${journalid}),Summary(${summary}),Date(${date})` + '</li>';
        });

        htmlList += '</ul>';
        document.getElementsByTagName("body").item(0).innerHTML += htmlList;
    }
}

function addArticleBtnClicked() {
    const journalName = document.getElementById("journal-name").value;
    const articleSummary = document.getElementById("article-summary").value;

    const postRequestBody = "action=addArticle&journal=" + journalName + "&summary=" + articleSummary;
    const postRequest = new XMLHttpRequest();

    postRequest.open("POST", "../home");
    postRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    postRequest.send(postRequestBody);

    postRequest.onload = function() {
    }
}
