function clicked() {
    const postRequestBody = "action=assign&devName=" + "D1" + "&projects=" + "project1,";
    const postRequest = new XMLHttpRequest();

    postRequest.open("POST", "../home");
    postRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    postRequest.send(postRequestBody);

    postRequest.onload = function() {
        console.log("Hey!");
    }
}
