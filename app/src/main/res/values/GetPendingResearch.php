<?php
$conn = mysqli_connect("localhost", "root", "", "bluevault");

// Query all 3 tables for status = 3 (Pending)
$sql = "SELECT Title, AUTHORS, 'SECA' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI, RsID FROM seca WHERE Status = 3
        UNION ALL
        SELECT Title, AUTHORS, 'SASE' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI, RsID FROM sase WHERE Status = 3
        UNION ALL
        SELECT Title, AUTHORS, 'SBMA' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI, RsID FROM sbma WHERE Status = 3
        ORDER BY DATE DESC";

$result = mysqli_query($conn, $sql);
$list = array();

while($row = mysqli_fetch_assoc($result)) {
    $list[] = array(
        'title' => $row['Title'],
        'author' => $row['AUTHORS'],
        'school' => $row['School'],
        'course' => $row['Course'],
        'date' => $row['DATE'],
        'status' => 3,
        'abstract' => $row['ABSTRACT'],
        'tags' => $row['TAGS'],
        'doi' => $row['DOI'],
        'rsid' => $row['RsID']
    );
}

header('Content-Type: application/json');
echo json_encode($list);
mysqli_close($conn);
?>