function onSubmitCourseNameBtnClicked() {
    const course = document.getElementById("course-name").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../../server/controller/controller.php?func=participants&course=" + course, true);
    getRequest.send();

    getRequest.onload = function() {
        document.getElementById("participants").value = this.responseText;
    }
}

function onSubmitStudentNameBtnClicked() {
    const student = document.getElementById("student").value;

    const getRequest = new XMLHttpRequest();
    getRequest.open("GET", "../../server/controller/controller.php?func=courses&student=" + student, true);
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

        resultArray.forEach(course => {
            const id = course["id"];
            const professorid = course["professorid"];
            const coursename = course["coursename"];
            const participants = course["participants"];
            const grades = course["grades"];

            htmlList += '<li>' + `id(${id}),professorid(${professorid}),coursename(${coursename}),participants(${participants}),grades(${grades})` + '</li>';
        });

        htmlList += '</ul>';
        document.getElementsByTagName("body").item(0).innerHTML += htmlList;
    }
}

function onSubmitAssignBtnClicked() {
    const course = document.getElementById("assign-course").value;
    const student = document.getElementById("assign-student").value;
    const grade = document.getElementById("assign-grade").value;

    const postRequestBody = "func=assign&course=" + course + "&student=" + student + "&grade=" + grade;
    const postRequest = new XMLHttpRequest();

    postRequest.open("POST", "../../server/controller/controller.php");
    postRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    postRequest.send(postRequestBody);
}
