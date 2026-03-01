<?php



class DBUtils {
	private $host = '127.0.0.1';
	private $db   = 'wp';
	private $user = 'root';
	private $pass = '';
	private $charset = 'utf8';	

	private $pdo;
	private $error;

	public function __construct () {
		$dsn = "mysql:host=$this->host;dbname=$this->db;charset=$this->charset";
		$opt = array(PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
			PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
			PDO::ATTR_EMULATE_PREPARES   => false);
		try {
			$this->pdo = new PDO($dsn, $this->user, $this->pass, $opt);		
		} // Catch any errors
		catch(PDOException $e){
			$this->error = $e->getMessage();
			echo "Error connecting to DB: " . $this->error;
		}
	}

	public function select() {
		$stmt = $this->pdo->query("SELECT * FROM Students");
		return $stmt->fetchAll(PDO::FETCH_ASSOC);
	}	

	public function selectStudent($name) {
        $stmt = $this->pdo->query("SELECT * FROM Students where name='" . $name ."'");
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function selectAllStudents() {
    	$stmt = $this->pdo->query("SELECT * FROM Students");
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function selectGradeForStudent($studentid) {
    	$stmt = $this->pdo->query("SELECT * FROM Grades where studentid=" . $studentid);
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

	public function insert($id, $value) {
		$affected_rows = $this->pdo->exec("INSERT into table values(" . $id . ",'" . $value ."');");
		return $affected_rows;
	}

	public function delete ($id) {
		$affected_rows = $this->pdo->exec("DELETE from table where id=" . $id);
		return $affected_rows;
	}

	public function update ($id, $value) {
		$affected_rows = $this->pdo->exec("UPDATE table SET field='" . $value ."' where id=" . $id);

	}
}
 

?>

