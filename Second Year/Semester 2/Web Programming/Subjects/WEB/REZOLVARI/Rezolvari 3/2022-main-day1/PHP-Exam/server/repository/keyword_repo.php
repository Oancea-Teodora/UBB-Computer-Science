<?php
require_once "../model/keyword.php";

class KeywordRepo {
    private $hostname = "localhost:3307";
    private $username = "root";
    private $password = "";
    private $database = "web-main1";
    private $tableName = "Keywords";
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

    public function getAll() {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "`;";
        $answerArray = array();

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        if ($result->num_rows > 0) {
            // output data from each row
            while($row = $result->fetch_assoc()) {
                $keyword = new Keyword((int) $row["id"], $row["_key"], $row["value"]);
                array_push($answerArray, json_encode($keyword));
            }
        }
        else {
            return $answerArray;
        } 

        return json_encode($answerArray);
    }

    public function save(Keyword $keyword) {
        $sqlQuery = "INSERT INTO `" . $this->tableName . "` (`_key`, `value`) VALUES (?, ?)";

        $statement = $this->connection->prepare($sqlQuery);

        $key = $keyword->getKey();
        $value = $keyword->getValue();

        $statement->bind_param("ss", $key, $value);
        $statement->execute();
    }
}
?>
