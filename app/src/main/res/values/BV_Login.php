<?php
// Database Connection
$host = "localhost";
$user = "root";
$pass = "";
$db   = "bluevault";

$conn = mysqli_connect($host, $user, $pass, $db);

if (!$conn) {
    die("Database Connection Error: " . mysqli_connect_error());
}

$idInput = $_POST['id_input'];
$password = $_POST['password'];

// 1. Logic for ADMIN (A) and SUPER ADMIN (S)
if (strpos($idInput, 'A') === 0 || strpos($idInput, 'S') === 0) {
    $query = "SELECT * FROM admins WHERE Admin_studID = '$idInput' AND Adpword = '$password'";
    $result = mysqli_query($conn, $query);
    
    if (mysqli_num_rows($result) > 0) {
        // Simple string return for Admin/Super Admin
        if (strpos($idInput, 'A') === 0) {
            echo "role_admin";
        } else {
            echo "role_super_admin";
        }
    } else {
        echo "Invalid Admin Credentials";
    }
} 
// 2. Logic for STUDENT (User)
else {
    $query = "SELECT * FROM registration WHERE IDnumber = '$idInput' AND Pword = '$password'";
    $result = mysqli_query($conn, $query);
    
    // ... inside your BV_Login.php Student Logic block ...
if (mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_assoc($result);
    $fullName = $row['StudName'] . " " . $row['StudLast'];
    
    // role_user | Name | ID | Email | School
    // Ensure 'school' matches the exact column name in your registration table
   echo "role_user|" . $fullName . "|" . $row['IDnumber'] . "|" . $row['email'] . "|" . $row['school'];
} else {
    echo "Invalid Student Credentials";
}
}

mysqli_close($conn);
?>