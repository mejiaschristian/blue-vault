<?php
$conn = mysqli_connect("localhost", "root", "", "bluevault");

// Query all 3 tables for status = 1 (Approved)
$sql = "SELECT RsID, Title, AUTHORS, 'SECA' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI FROM seca WHERE Status = 1
        UNION ALL
        SELECT RsID, Title, AUTHORS, 'SASE' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI FROM sase WHERE Status = 1
        UNION ALL
        SELECT RsID, Title, AUTHORS, 'SBMA' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI FROM sbma WHERE Status = 1
        ORDER BY DATE DESC";

$result = mysqli_query($conn, $sql);
$list = array();

while($row = mysqli_fetch_assoc($result)) {
    $list[] = array(
        'rsid' => $row['RsID'],
        'title' => $row['Title'],
        'author' => $row['AUTHORS'],
        'school' => $row['School'],
        'course' => $row['Course'],
        'date' => $row['DATE'],
        'status' => 1,
        'abstract' => $row['ABSTRACT'],
        'tags' => $row['TAGS'],
        'doi' => $row['DOI']
    );
}

header('Content-Type: application/json');
echo json_encode($list);
mysqli_close($conn);
?>