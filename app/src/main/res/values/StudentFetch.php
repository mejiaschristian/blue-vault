<?php
$conn = mysqli_connect("localhost", "root", "", "bluevault");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Fetching only from the registration table
$sql = "SELECT StudName, StudLast, IDnumber, department FROM registration";
$result = mysqli_query($conn, $sql);

$students = array();

if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        // Combine names: Lastname, Firstname
        $fullName = $row['StudLast'] . ", " . $row['StudName'];
        
        $students[] = array(
            'name' => $fullName,
            'id'   => $row['IDnumber'],
            'dept' => $row['department']
        );
    }
}

header('Content-Type: application/json');
echo json_encode($students);
mysqli_close($conn);
?>