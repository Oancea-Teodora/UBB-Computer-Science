function onSubmitNameBtnClicked() {
    const userName = document.getElementById("user-name").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../home?action=submitName&name=" + userName, true);
    getRequest.send();

    getRequest.onload = function() {
        document.location.href = "../pages/userpage.jsp";
    }
}
