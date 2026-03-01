<?php
require_once "../model/person.php";

class PersonRepository {
    private $hostname = "localhost:3307";
    private $username = "root";
    private $password = "";
    private $database = "web-ex6";
    private $tableName = "Persons";
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

    public function selectPersonByName(string $name) {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "` WHERE `name` = '" . $name . "';";

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        if ($result->num_rows > 0) {
            // output data from each row
            while($row = $result->fetch_assoc()) {
                $person = new Person((int) $row["id"], $row["name"], (int) $row["age"], $row["gender"]);
                return $person;
            }
        }
        
        return null;
    }
}
?>
