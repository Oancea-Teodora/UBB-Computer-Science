function onSubmitPersonNameBtnClicked() {
    const name = document.getElementById("person-name").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../../server/controller/controller.php?func=channels&name=" + name, true);
    getRequest.send();

    getRequest.onload = function() {
        let resultArray;

        try {
            resultArray = JSON.parse(this.responseText);
            resultArray.forEach(function(current, index, array) {
                array[index] = JSON.parse(current);
            });
        } catch(e) {
            const ulElem = document.getElementsByTagName("ul");
            if (ulElem.length > 0) {
                document.getElementsByTagName("body").item(0).removeChild(ulElem[0])
            }

            return;
        }

        const ulElem = document.getElementsByTagName("ul");
        if (ulElem.length > 0) {
            document.getElementsByTagName("body").item(0).removeChild(ulElem[0])
        }

        let htmlList = '<ul>';

        resultArray.forEach(article => {
            const id = article["id"];
            const ownerid = article["ownerId"];
            const name = article["name"];
            const description = article["description"];
            const subscribers = article["subscribers"];

            htmlList += '<li>' + `id(${id}),ownerid(${ownerid}),name(${name}),description(${description}),subscribers(${subscribers})` + '</li>';
        });

        htmlList += '</ul>';
        document.getElementsByTagName("body").item(0).innerHTML += htmlList;
    }
}

function onSeeSubscribedChannelBtnClicked() {
    const name = document.getElementById("person-name").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../../server/controller/controller.php?func=sub", true);
    getRequest.send();

    getRequest.onload = function() {
        let resultArray;

        try {
            resultArray = JSON.parse(this.responseText);
            resultArray.forEach(function(current, index, array) {
                array[index] = JSON.parse(current);
            });
        } catch(e) {
            const ulElem = document.getElementsByTagName("ol");
            if (ulElem.length > 0) {
                document.getElementsByTagName("body").item(0).removeChild(ulElem[0])
            }

            return;
        }

        const ulElem = document.getElementsByTagName("ol");
        if (ulElem.length > 0) {
            document.getElementsByTagName("body").item(0).removeChild(ulElem[0])
        }

        let htmlList = '<ol>';

        resultArray.forEach(article => {
            const id = article["id"];
            const ownerid = article["ownerId"];
            const name = article["name"];
            const description = article["description"];
            const subscribers = article["subscribers"];

            htmlList += '<li>' + `name(${name}),description(${description})` + '</li>';
        });

        htmlList += '</ol>';
        document.getElementsByTagName("body").item(0).innerHTML += htmlList;
    }
}

function onSubscribeBtnClicked() {
    const channel = document.getElementById("channel-id").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../../server/controller/controller.php?func=new&channel=" + channel, true);
    getRequest.send();

    getRequest.onload = function() {
    }
}
