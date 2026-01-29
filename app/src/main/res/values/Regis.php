<?php
$conn = mysqli_connect("localhost", "root", "", "bluevault");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Get POST data from Android app
$email = $_POST['email'];
$lastName = $_POST['lastName'];
$firstName = $_POST['firstName'];
$idNumber = $_POST['idNumber'];
$password = $_POST['password'];
$school = $_POST['school']; // This matches the 'school' column in your DB
$course = $_POST['course'];

// 1. Check if ID Number or Email already exists
$checkQuery = "SELECT * FROM registration WHERE IDnumber = '$idNumber' OR email = '$email'";
$checkResult = mysqli_query($conn, $checkQuery);

if (mysqli_num_rows($checkResult) > 0) {
    echo "Account already exists with this ID or Email";
} else {
    // 2. Insert into registration (using 'school' column as requested)
    $sql = "INSERT INTO registration (email, StudLast, StudName, IDnumber, pword, school, course) 
            VALUES ('$email', '$lastName', '$firstName', '$idNumber', '$password', '$school', '$course')";

    if (mysqli_query($conn, $sql)) {
        echo "success";
    } else {
        echo "Registration Error: " . mysqli_error($conn);
    }
}

mysqli_close($conn);
?>