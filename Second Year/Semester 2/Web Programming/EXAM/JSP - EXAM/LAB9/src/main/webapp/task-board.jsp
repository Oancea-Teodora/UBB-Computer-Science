<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <html>

        <head>
            <title>Task Board</title>
            <script>
                var contextPath = '<%= request.getContextPath() %>';

                function updateTaskStatus(taskId, newStatus) {
                    var xhr = new XMLHttpRequest();
                    xhr.open('POST', contextPath + '/tasks', true);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState === 4) {
                            if (xhr.status === 200) {
                                loadTasks();
                                updateTasksMovedCount();
                            } else {
                                alert('Failed to update task status');
                            }
                        }
                    };
                    xhr.send('taskId=' + taskId + '&newStatus=' + newStatus);
                }

                function loadTasks() {
                    var xhr = new XMLHttpRequest();
                    xhr.open('GET', contextPath + '/api/tasks', true);
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState === 4 && xhr.status === 200) {
                            var tasks = JSON.parse(xhr.responseText);
                            updateTaskBoard(tasks);
                        }
                    };
                    xhr.send();
                }

                function updateTaskBoard(tasks) {
                    document.getElementById('todo-tasks').innerHTML = '';
                    document.getElementById('inprogress-tasks').innerHTML = '';
                    document.getElementById('done-tasks').innerHTML = '';

                    tasks.forEach(function (task) {
                        var taskDiv = document.createElement('div');

                        taskDiv.title = task.lastUpdatedByUsername ? 'Last updated by: ' + task.lastUpdatedByUsername : '';

                        taskDiv.innerHTML = '<strong>' + task.title + '</strong><br>ID: ' + task.id;

                        if (task.status === 'todo') {
                            taskDiv.onclick = function () { moveTask(task.id, 'in_progress'); };
                            taskDiv.innerHTML += '<br><em>Click to move to In Progress</em>';
                            document.getElementById('todo-tasks').appendChild(taskDiv);
                        } else if (task.status === 'in_progress') {
                            taskDiv.innerHTML += '<br><button onclick="moveTask(' + task.id + ', \'todo\')">← To Do</button> ';
                            taskDiv.innerHTML += '<button onclick="moveTask(' + task.id + ', \'done\')">Done →</button>';
                            document.getElementById('inprogress-tasks').appendChild(taskDiv);
                        } else if (task.status === 'done') {
                            taskDiv.onclick = function () { moveTask(task.id, 'in_progress'); };
                            taskDiv.innerHTML += '<br><em>Click to move back to In Progress</em>';
                            document.getElementById('done-tasks').appendChild(taskDiv);
                        }
                    });
                }

                function moveTask(taskId, newStatus) {
                    updateTaskStatus(taskId, newStatus);
                }

                function updateTasksMovedCount() {
                    location.reload();
                }

                setInterval(loadTasks, 2000); // Auto-refresh every 2 seconds

                window.onload = function () {
                    loadTasks();
                };
            </script>
        </head>

        <body>
            <h1>Task Management Board</h1>
            <p>Welcome, <strong>${sessionScope.user.username}</strong>!</p>
            <p>Tasks moved this session: <strong>${sessionScope.tasksMovedCount}</strong></p>
            <a href="login">Logout</a>

            <table border="1" width="100%">
                <tr>
                    <th width="33%">To Do</th>
                    <th width="33%">In Progress</th>
                    <th width="33%">Done</th>
                </tr>
                <tr valign="top">
                    <td height="400px">
                        <div id="todo-tasks"></div>
                    </td>
                    <td height="400px">
                        <div id="inprogress-tasks"></div>
                    </td>
                    <td height="400px">
                        <div id="done-tasks"></div>
                    </td>
                </tr>
            </table>
        </body>

        </html>