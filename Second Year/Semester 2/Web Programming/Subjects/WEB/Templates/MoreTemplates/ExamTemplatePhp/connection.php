<?php
 
    $con=mysqli_connect('localhost','root','','exam');
 
    if(!$con)
    {
        die('Connection error.'.mysqli_error($con));
    }
?>