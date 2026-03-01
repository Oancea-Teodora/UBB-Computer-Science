function onSubmitNameBtnClicked() {
    const userName = document.getElementById("user-name").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "./server/controller/controller.php?func=login&user=" + userName, true);
    getRequest.send();

    getRequest.onload = function() {
        // document.location.href = "./client/pages/user.html";
    }
}
