<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Tic Tac Toe - Login</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                max-width: 400px;
                margin: 50px auto;
                padding: 20px;
            }

            .form-group {
                margin-bottom: 15px;
            }

            label {
                display: block;
                margin-bottom: 5px;
            }

            input[type="text"],
            input[type="password"] {
                width: 100%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }

            input[type="submit"] {
                background-color: #4CAF50;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                width: 100%;
            }

            input[type="submit"]:hover {
                background-color: #45a049;
            }

            .error {
                color: red;
                margin-bottom: 15px;
            }

            .info {
                background-color: #f0f0f0;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 15px;
            }
        </style>
    </head>

    <body>
        <h1>Tic Tac Toe - Login</h1>

        <div class="info">
            <strong>Test Users:</strong><br>
            Username: player1, Password: pass1<br>
            Username: player2, Password: pass2<br>
            Username: admin, Password: admin
        </div>

        <% if (request.getAttribute("error") !=null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

                <form action="login" method="post" onsubmit="return validateForm()">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>

                    <input type="submit" value="Login">
                </form>

                <script>
                    function validateForm() {
                        var username = document.getElementById("username").value.trim();
                        var password = document.getElementById("password").value.trim();

                        if (username === "" || password === "") {
                            alert("Please fill in both username and password.");
                            return false;
                        }

                        if (username.length < 3) {
                            alert("Username must be at least 3 characters long.");
                            return false;
                        }

                        return true;
                    }
                </script>
    </body>

    </html>