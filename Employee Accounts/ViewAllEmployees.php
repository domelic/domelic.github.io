<!DOCTYPE html>

<?php
require_once "MySQLConnectionInfo.php";
session_start(); 

if (!isset($_SESSION["emailAddress"]) && !isset($_SESSION["password"])) {
    header("Location: Login.php");
  }
$connection = mysqli_connect($host, $username, $password, $database);
if (!$connection)
{
    die("Connection could not be established to database: " . $host);
}
mysqli_select_db($connection);
$sqlquery = "SELECT * FROM employee";
$result = mysqli_query($connection, $sqlquery);
$numberOfRows = mysqli_num_rows($result);
$message =
    "<div><table width=\"100%\" border=\"1\">
    <h4>Database Data</h4><br>
    <tr><th>First Name</th><th>Last Name</th><th>Email Address</th>
    <th>Phone Number</th><th>SIN</th><th>Password</th></tr>";
for($i = 0; $i < $numberOfRows; $i++) {
    $row = mysqli_fetch_row($result);
    $message .= 
        "<tr><td>" . $row[1] . "</td><td>"
        . $row[2] . "</td><td>" . $row[3] 
        . "</td><td>" . $row[4] . "</td><td>" 
        . $row[5] . "</td><td>" . $row[6] 
        . "</td></tr>";
}
$message .= "</table></div>";
?>

<html lang="en">

<head>
    <meta charset="utf-8">
    <title>View All Employees</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
    <script scr="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="stylesheet.css">
    <link rel="shortcut icon" href="images/favicon.ico">
</head>

<body>
    <?php
    include_once "Header.php";
    include_once "Menu.php";
    ?>
    <div class="container-fluid">
        <div class="container">
            <div style="margin:150px">
                <?php
                    echo "<h4>Session State Data</h4>";
                    echo "<br>First name: ";
                    echo $_SESSION["firstName"];
                    echo "<br>Last name: ";
                    echo $_SESSION["lastName"];
                    echo "<br>Email: ";
                    echo $_SESSION["emailAddress"];
                    echo "<br>Phone number: ";
                    echo $_SESSION["telephoneNumber"];
                    echo "<br>SIN: ";
                    echo $_SESSION["SIN"];
                    echo "<br>Password: ";
                    echo $_SESSION["password"];

                    echo "<hr>";
                    echo $message;
                ?>
            </div>
        </div>
    </div>
    <?php
    include_once "Footer.php";
    ?>
</body>

</html>
