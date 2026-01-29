<?php
$conn = mysqli_connect("localhost", "root", "", "bluevault");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Data from Android
$idNumber = $_POST['id_number'];
$school   = $_POST['school']; // This will be "seca", "sase", or "sbma"
$title    = $_POST['title'];
$authors  = $_POST['authors'];
$abstract = $_POST['abstract'];
$tags     = $_POST['tags'];
$doi      = $_POST['doi'];
$date     = date("Y-m-d");

// Validate that the school is allowed to prevent SQL injection on table names
$allowedTables = ['seca', 'sase', 'sbma'];
if (!in_array($school, $allowedTables)) {
    die("Invalid school table");
}

// Insert into the dynamic table based on user's school
// Using your screenshot columns: Title, AUTHORS, ABSTRACT, TAGS, DOI, DATE, IDnumber
$sql = "INSERT INTO $school (Title, AUTHORS, ABSTRACT, TAGS, DOI, DATE, IDnumber) 
        VALUES ('$title', '$authors', '$abstract', '$tags', '$doi', '$date', '$idNumber')";

if (mysqli_query($conn, $sql)) {
    echo "success";
} else {
    echo "Error: " . mysqli_error($conn);
}

mysqli_close($conn);
?>