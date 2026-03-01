<?php
require_once "../model/course.php";

class CourseRepo {
    private $hostname = "localhost:3307";
    private $username = "root";
    private $password = "";
    private $database = "web-ex5";
    private $tableName = "Courses";
    private $connection = NULL;

    public function __construct() {
        $this->connect();
    }

    public function __destruct() {
        $this->connection->close();
    }

    private function connect() {
        $this->connection = new mysqli($this->hostname, $this->username, $this->password, $this->database);
        
        // check connection
        if ($this->connection->connect_error) {
            die("Connection failed: " . $this->connection->connect_error);
        }
    }

    public function getParticipantsToCourse(string $name) {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "` WHERE `coursename` = '" . $name . "';";

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            return $row["participants"];
        }

        return "";
    }

    public function getCoursesWithParticipant(string $student) {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "`;";
        $answerArray = array();

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        if ($result->num_rows > 0) {
            // output data from each row
            while($row = $result->fetch_assoc()) {
                $course = new Course((int) $row["id"], (int) $row["professorid"], $row["coursename"], $row["participants"], $row["grades"]);
                if (in_array($student, explode(",", $row["participants"]))) {
                    array_push($answerArray, json_encode($course));
                }
            }
        }
        else {
            return $answerArray;
        } 

        return json_encode($answerArray);
    }

    public function assignGradeToStudent(string $coursename, string $grade, string $student) {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "` WHERE `coursename` = '" . $coursename . "';";

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        $course = null;
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $course = new Course((int) $row["id"], (int) $row["professorid"], $row["coursename"], $row["participants"], $row["grades"]);
        }
        else {
            return;
        }

        $newParticipants = $course->getParticipants();
        $newGrades = "";

        if (!in_array($student, explode(",", $newParticipants))) {
            $newParticipants = $newParticipants . $student . ",";
        }
        
        $gradeArr = explode(",", $course->getGrades());
        $found = false;

        foreach($gradeArr as $elem) {
            $arr = explode(":", $elem);
            if (count($arr) != 2) {
                continue;
            }

            if ($arr[0] == $student) {
                $found = true;
                $newGrades = $newGrades . $arr[0] . ":" . $grade . ",";
            }
            else {
                $newGrades = $newGrades . $elem . ",";
            }   
        }

        if (!$found) {
            $newGrades = $newGrades . $student . ":" . $grade . ",";
        }
        
        $sqlQuery = "UPDATE `" . $this->tableName . "` SET `participants` = '" . $newParticipants . "', `grades` = '" . $newGrades . "' WHERE `coursename` = '" . $coursename . "'";
        $this->connection->query($sqlQuery);
    }
}
?>
