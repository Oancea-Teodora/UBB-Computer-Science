<?php 

$mysqli = new mysqli('localhost','root','','exam') or die(mysqli_error($mysqli));

if(isset($_POST['array'])) {
	$asset = $_POST['array'];

	for ($i = 0; $i < count($asset); $i++) {
  		$object = json_decode($asset[$i]);

  		$id = $object->{'id'};
		$name = $object->{'name'};
		$description = $object->{'description'};
		$value = $object->{'value'};

		$mysqli->query("insert into assets (userid, name, description, value) values ($id, '$name', '$description', $value)") or die ($mysqli->error);
	} 
}

?>