$(document).ready(() => {
    let last;

    $.get(
        "./FindLastAddedPost",
        {

        },
        (response) => {
            last = response;
            console.log(last);

            setInterval(function () {
                $.get(
                    "./FindLastAddedPost",
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

function insertAndUpdatePost(url) {
    const id = document.getElementById("post-id").value;
    const name = document.getElementById("post-topic-name").value;
    const text = document.getElementById("post-text").value;

    const postRequestBody = "id=" + id + "&topicName=" + name + "&text=" + text;
    const postRequest = new XMLHttpRequest();

    postRequest.open("POST", url);
    postRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    postRequest.send(postRequestBody);
}

function onUpdateBtnClicked() {
    insertAndUpdatePost("./UpdatePost");
}

function onInsertBtnClicked() {
    insertAndUpdatePost("./InsertPost");
}
