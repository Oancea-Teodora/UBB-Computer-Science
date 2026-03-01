<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Main page</title>

    <style>
        a {
            text-decoration: none;
            color: blue;
            background-color: none;
        }

        .add {
            color: green;
        }

        .edit {
            color: purple;
        }

        .search {
            color: orange;
        }

        .all {
            color: orange;
        }
    </style>

</head>
<body>

<?php
    session_start();
 
    if(isset($_SESSION['User']))
    {
        echo ' Welcome, ' . $_SESSION['User'].'<br/>';
        echo '<button><a href="logout.php?logout" style="text-decoration:none;">Logout</a></button><br/><br/>';
    }
    else
    {
        header("location:index.php");
    }
 
?>

<?php require_once 'process.php'; ?> 

<?php 
        
    $mysqli = new mysqli('localhost','root','','exam') or die(mysqli_error($mysqli));

    $sql = "select * from students";

    if(isset($_POST['search'])) {
        $search_term = $_POST['search_box'];

        $sql = "select * from students where name = '$search_term'";
    }

    $result = $mysqli->query($sql) or die($mysqli->error);
        
?>

<form name="search_form" action="main.php" method="POST" class="form-filter">
    <label>Filter by name:&nbsp;</label>
    <input type="text" name="search_box" placeholder="Enter name" value="">
    <button type="submit" name="search" class="search"> Filter </button>
    <button type="submit" name="all" class="all" > Show all </button>
    <br><br>
</form>

<div>
    <div>
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Grade</th>
                    <th colspan="2">Action</th>
                </tr>
            </thead>

        <?php 
            while($row = $result->fetch_assoc()):
        ?>

            <tr>
                <td><?php echo $row['name']; ?></td>
                <td><?php echo $row['grade']; ?></td>
                <td>
                    <button style="text-decoration: none">
                        <a href="main.php?update=<?php echo $row['id']; ?>" >Update</a>
                    </button>
                    <button style="text-decoration: none" >
                        <a href="process.php?delete=<?php echo $row['id']; ?>" 
                            onclick="return confirm('Are you sure you want to remove this item?');">Delete</a>
                    </button>
                </td>
            </tr>

        <?php endwhile; ?>

        </table>
    </div>

    <br><br>
    <form action="" method="POST" class="form-submit"> 
        <input type="hidden" name="id" value="<?php echo $id; ?>" class="id">
        <label>Name</label>
        <input type="text" name="name" placeholder="Enter name" class="name" value="<?php echo $name ?>"><br>
        <label>Grade</label>
        <input type="text" name="grade" value="<?php echo $grade; ?>" class="grade" placeholder="Enter grade" ><br><br>
        <?php 
            if ($edit == true): 
        ?>
            <button type="submit" name="edit" class="edit" >Update</button>
        <?php 
            else:
        ?>
            <button type="submit" name="save" class="add" >Save</button>
        <?php endif; ?>
    </form>
</div>

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<!-- Popper JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
    $(document).ready(function(){

            $(".add").click(function(e){
                e.preventDefault();
                //get all the values from the form 
                var $form = $(this).closest(".form-submit");
                var addName = $form.find(".name").val();
                var addGrade = $form.find(".grade").val();

                $.ajax({
                    url: 'process.php', 
                    method: 'post',
                    data: {addName:addName, addGrade:addGrade},
                    success: function(response){
                        window.scrollTo(0,0);
                        alert("Added successfully");
                        location.reload();
                    }
                });
            });

            $(".edit").click(function(e){
                e.preventDefault();
                //get all the values from the form 
                var $form = $(this).closest(".form-submit");
                var editId = $form.find(".id").val();
                var editName = $form.find(".name").val();
                var editGrade = $form.find(".grade").val();

                $.ajax({
                    url: 'process.php', 
                    method: 'post',
                    data: {editId:editId, editName:editName, editGrade:editGrade},
                    success: function(response){
                        window.scrollTo(0,0);
                        alert("Updated successfully");
                        window.location.href = "main.php";
                    }
                });
            });
    });


</script>


</body>
</html>

