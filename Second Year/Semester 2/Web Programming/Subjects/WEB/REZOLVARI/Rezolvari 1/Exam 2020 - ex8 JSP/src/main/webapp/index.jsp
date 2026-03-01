<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
<div class="d-flex align-items-center justify-content-center" style="height: 600px">
    <form  class="form-group" action="LoginController" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" name="username" class="form-control" id="username" aria-describedby="emailHelp" placeholder="Enter username" required>
        </div>

        <div class="form-group">
            <label for="parent">Mother/ father name</label>
            <input type="text" name="parent" class="form-control" id="parent" placeholder="Enter parent name" required>
        </div>

        <button type="submit" class="btn btn-primary">Login</button>
    </form>
</div>
</body></html>