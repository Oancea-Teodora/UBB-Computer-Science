$(document).ready(() => {
    addRowHandlers();
})

function onAddKeywordBtn() {
    const key = document.getElementById("key").value;
    const value = document.getElementById("value").value;

    const postRequestBody = "action=add&key=" + key + "&value=" + value;
    const postRequest = new XMLHttpRequest();

    postRequest.open("POST", "../home");
    postRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    postRequest.send(postRequestBody);

    postRequest.onload = function() {
    }
}

function onSearchBtn() {
    const title = document.getElementById("title").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../home?action=search&title=" + title, true);
    getRequest.send();

    getRequest.onload = function() {
        const table = document.getElementById('docs-table');
        while(table.rows.length > 1) {
            table.deleteRow(1);
        }

        let resultArray = JSON.parse(this.responseText);

        resultArray.forEach(article => {
            const id = article["id"];
            const title = article["title"];
            const templates = article["templates"];

            let row = table.insertRow();

            let idRow = row.insertCell(0);
            idRow.innerHTML = id;

            let titleRow = row.insertCell(1);
            titleRow.innerHTML = title;

            let templatesRow = row.insertCell(2);
            templatesRow.innerHTML = templates;
        });

        addRowHandlers();
    }
}

function rowHandler(row) {
    const cellTdElements = row.getElementsByTagName("td");
    if (cellTdElements.length < 3) {
        return;
    }

    const id = cellTdElements[0].innerHTML;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../home?action=render&id=" + id, true);
    getRequest.send();

    getRequest.onload = function() {
        document.getElementById('render').value = this.responseText;
    }
}

function addRowHandlers() {
    const table = document.getElementById('docs-table');
    const rows = table.getElementsByTagName("tr");

    for (let i = 0; i < rows.length; i++) {
        const currentRow = table.rows[i];

        currentRow.onclick = function () {
            rowHandler(currentRow);
        };
    }
}
