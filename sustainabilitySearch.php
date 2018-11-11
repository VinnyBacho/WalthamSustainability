<?php

$host = '127.0.0.1';
$dbname= 'cs380';
$username = 'root';
$password = '';

var_dump($_POST);

try {
    // Connect to MySQL, select database
    $con = mysqli_connect($host,$username,$password, $dbname);
    
} catch (Exception $e) {
    $error_message = $e->getMessage();
    exit();
}

$sql = "SELECT * FROM sustainability WHERE ADDRESS = '175 Forest Street'";
$result = mysqli_query($con, $sql);

while($line = mysqli_fetch_array($result, MYSQLI_ASSOC)){
    echo "<h1>Beep Boopo" . $line['address'] . "<h1>";
}
?>