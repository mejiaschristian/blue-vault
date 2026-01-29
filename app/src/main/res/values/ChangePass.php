<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "bluevault";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Database connection failed");
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // These keys must match the params.put() keys in Java
    $userId = $_POST['user_id'];
    $oldPassInput = $_POST['old_password'];
    $newPassInput = $_POST['new_password'];

    // 1. Fetch using correct column names: pword and IDnumber
    $stmt = $conn->prepare("SELECT pword FROM registration WHERE IDnumber = ?");
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        // FIXED: Changed ['password'] to ['pword'] to match your SELECT query
        $dbPassword = $row['pword'];

        // 2. Verify
        if ($oldPassInput === $dbPassword) {

            // 3. Update using correct column names
            $updateStmt = $conn->prepare("UPDATE registration SET pword = ? WHERE IDnumber = ?");
            $updateStmt->bind_param("ss", $newPassInput, $userId);

            if ($updateStmt->execute()) {
                echo "success";
            } else {
                echo "update_failed";
            }
            $updateStmt->close();
        } else {
            echo "incorrect_old";
        }
    } else {
        echo "user_not_found";
    }

    $stmt->close();
    $conn->close();
}
