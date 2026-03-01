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

        .red {
            color: red;
        }

        .black {
            color: black;
        }

        #generate {
            color: white;
            background-color: red;
        }
    </style>
</head>
<body>

<?php
    session_start();
 
    if(isset($_SESSION['User']))
    {
        //echo ' Welcome, ' . $_SESSION['User'].'<br/>';
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
    $id = $_SESSION['User'];

    $sql = "select * from assets where userid=$id";

    $result = $mysqli->query($sql) or die($mysqli->error);
        
?>

<script>
        function generateFields() {
                var id_input = document.createElement("INPUT");
                id_input.setAttribute("type", "hidden");
                id_input.setAttribute("name", "id");
                id_input.setAttribute("value", "<?php echo $id ?>");
                id_input.setAttribute("class", "id");
                document.getElementById("form").appendChild(id_input);

                var name_input = document.createElement("INPUT");
                name_input.setAttribute("type", "text");
                name_input.setAttribute("name", "name");
                name_input.setAttribute("placeholder", "Enter name");
                name_input.setAttribute("value", "");
                name_input.setAttribute("class", "name");
                document.getElementById("form").appendChild(name_input);

                var description_input = document.createElement("INPUT");
                description_input.setAttribute("type", "text");
                description_input.setAttribute("name", "description");
                description_input.setAttribute("placeholder", "Enter description");
                description_input.setAttribute("value", "");
                description_input.setAttribute("class", "description");
                document.getElementById("form").appendChild(description_input);

                var value_input = document.createElement("INPUT");
                value_input.setAttribute("type", "text");
                value_input.setAttribute("name", "value");
                value_input.setAttribute("placeholder", "Enter value");
                value_input.setAttribute("value", "");
                value_input.setAttribute("class", "value");
                document.getElementById("form").appendChild(value_input);

                var br = document.createElement("br");
                var form = document.getElementById("form");
                form.appendChild(br);
        }
</script>

<div>
    <div>
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Value</th>
                </tr>
            </thead>

        <?php 
            while($row = $result->fetch_assoc()):
        ?>

            <tr>
                <td><?php echo $row['name']; ?></td>
                <td><?php echo $row['description']; ?></td>
                <?php 
                $value = $row['value'];
                $class = "";
                if ($value > 10) {
                    $class = "red";
                } else {
                    $class = "black";
                }
                ?>
                <td class="<?php echo $class; ?>"><?php echo $value; ?></td>
            </tr>

        <?php endwhile; ?>

        </table>
    </div>

    <br><br>
    <button onclick="generateFields()" id="generate">+</button><br>

    <br><br>
    <form action="" method="POST" class="form-submit" id="form"> 
        <input type="hidden" name="id" value="<?php echo $id; ?>" class="id">
        <label>Name</label>
        <input type="text" name="name" placeholder="Enter name" class="name" value=""><br>
        <label>Description</label>
        <input type="text" name="description" placeholder="Enter description" value="" class="description" ><br>
        <label>Value</label>
        <input type="text" name="value" placeholder="Enter value"  value="" class="value" ><br><br>

        <!--This form has initially "one" place for adding objects, but + button is able to generate more inputs-->
        <button type="submit" name="save" class="add" >Save</button><br><br>
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
                var addId = $('.id').map((_,el) => el.value).get();
                var addName = $('.name').map((_,el) => el.value).get();
                var addDescription = $('.description').map((_,el) => el.value).get();
                var addValue = $('.value').map((_,el) => el.value).get();
                var array = [];

                for (var i = 0; i < addId.length; i++) {
                    var asset = {id: addId[i], name: addName[i], description: addDescription[i], value: addValue[i]};
                    var myJSON = JSON.stringify(asset);
                    array.push(myJSON);
                }

                $.ajax({
                    url: 'process.php', 
                    method: 'post',
                    data: {array:array},
                    success: function(response){
                        window.scrollTo(0,0);
                        alert("Added successfully");
                        location.reload();
                    }
                });
            });
    });


</script>
</body>
</html>

