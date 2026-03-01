<?php
session_start();
if(isset($_SESSION['valid']))
    unset($_SESSION['valid']);
session_destroy();
?>
