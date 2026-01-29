<?php
$conn = mysqli_connect("localhost", "root", "", "bluevault");

// Query all 3 tables for status = 0 (Declined) OR status = 3 (Unpublished/Pending)
$sql = "SELECT RsID, Title, AUTHORS, 'SECA' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI, Status FROM seca WHERE Status IN (0, 3)
        UNION ALL
        SELECT RsID, Title, AUTHORS, 'SASE' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI, Status FROM sase WHERE Status IN (0, 3)
        UNION ALL
        SELECT RsID, Title, AUTHORS, 'SBMA' as School, 'N/A' as Course, DATE, ABSTRACT, TAGS, DOI, Status FROM sbma WHERE Status IN (0, 3)
        ORDER BY Status DESC, DATE DESC";

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
        'status' => $row['Status'],
        'abstract' => $row['ABSTRACT'],
        'tags' => $row['TAGS'],
        'doi' => $row['DOI']
    );
}

header('Content-Type: application/json');
echo json_encode($list);
mysqli_close($conn);
?>