<?php
session_start();

class Controller {
    public function __construct() {
    }

    public function serve() {
        if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "login") {
            $this->serveLogin();
        }
    }

    private function serveLogin() {
        if (!isset($_GET["user"])) {
            return;
        }
        
        // echo $_SESSION["user"]; 
        $_SESSION["user"] = $_GET["user"];  
    }
}

$controller = new Controller();
$controller->serve();
?>
