<?php 

$mysqli = new mysqli('localhost','root','','exam') or die(mysqli_error($mysqli));

$id = 0;
$edit = false;
$name = '';
$grade = '';

if(isset($_POST['addName'])) {
	$name = $_POST['addName'];
	$grade = $_POST['addGrade'];

	$mysqli->query("insert into students (name, grade) values ('$name', '$grade')") or die ($mysqli->error);
}

if(isset($_GET['delete'])) {
	$id = $_GET['delete'];

	$mysqli->query("delete from students where id=$id") or die ($mysqli->error);

	header("location: main.php");
}

if(isset($_GET['update'])) {
	$id = $_GET['update'];
	$edit = true;
	
	$result = $mysqli->query("select * from students where id=$id") or die ($mysqli->error);

	if ($result->num_rows) {
		$row = $result->fetch_array();
		$name = $row['name'];
		$grade = $row['grade'];
	}
}

if(isset($_POST['editId'])) {
	$id = $_POST['editId'];
	$name = $_POST['editName'];
	$grade = $_POST['editGrade'];

	$mysqli->query("update students set name='$name', grade=$grade where id=$id ") or die ($mysqli->error);
}


?>