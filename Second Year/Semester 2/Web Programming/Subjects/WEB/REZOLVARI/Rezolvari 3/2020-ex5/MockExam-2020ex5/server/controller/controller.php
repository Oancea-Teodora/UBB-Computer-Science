<?php
require_once "../repository/course_repo.php";

session_start();

class Controller {
    public function __construct() {
        $this->courseRepo = new CourseRepo();
    }

    public function serve() {
        if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "login") {
            $this->serveLogin();
        }
        else if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "participants") {
            $this->serveParticipants();
        }
        else if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "courses") {
            $this->serveCourses();
        }
        else if (isset($_POST["func"]) && $_POST["func"] === "assign")
            $this->serveAssign();
    }

    private function serveLogin() {
        if (!isset($_GET["user"])) {
            return;
        }
        
        // echo $_SESSION["user"]; 
        $_SESSION["user"] = $_GET["user"];  
    }

    private function serveParticipants() {
        if (!isset($_GET["course"])) {
            return;
        }

        echo $this->courseRepo->getParticipantsToCourse($_GET["course"]);
    }

    private function serveCourses() {
        if (!isset($_GET["student"])) {
            return;
        }

        echo $this->courseRepo->getCoursesWithParticipant($_GET["student"]);
    }

    private function serveAssign() {
        if (!isset($_POST["course"]) || !isset($_POST["grade"]) || !isset($_POST["student"])) {
            return;
        }

        $this->courseRepo->assignGradeToStudent($_POST["course"], $_POST["grade"], $_POST["student"]);
    }
}

$controller = new Controller();
$controller->serve();
?>
