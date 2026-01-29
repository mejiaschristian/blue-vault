<?php
$conn = mysqli_connect("localhost", "root", "", "bluevault");

$idNumber = $_GET['id_number'];
$school   = $_GET['school']; // 'seca', 'sase', or 'sbma'

$allowedTables = ['seca', 'sase', 'sbma'];
if (!in_array($school, $allowedTables)) {
    die(json_encode([]));
}

// Fetch research items where IDnumber matches the logged-in student
$sql = "SELECT * FROM $school WHERE IDnumber = '$idNumber' ORDER BY RsID DESC";
$result = mysqli_query($conn, $sql);

$researchList = array();

while($row = mysqli_fetch_assoc($result)) {
    $researchList[] = array('rsid' => $row['rsid'],
        'title' => $row['Title'],
        'author' => $row['AUTHORS'],
        'school' => strtoupper($school),
        'course' => "N/A", // Add a course column to your school tables if needed
        'date' => $row['DATE'],
        'status' => 3, // Defaulting to 3 (Pending) based on your ResearchItem.java logic
        'abstract' => $row['ABSTRACT'],
        'tags' => $row['TAGS'],
        'doi' => $row['DOI']
    );
}

header('Content-Type: application/json');
echo json_encode($researchList);
mysqli_close($conn);
?>