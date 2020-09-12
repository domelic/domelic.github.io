<!DOCTYPE html>

<?php
require_once "MySQLConnectionInfo.php";
session_start();

$message = "</br>";

$sessionFlag = 0;

$firstName = "";
$lastName = "";
$emailAddress = "";
$telephoneNumber = 0;
$SIN = 0;
$password = "";

if (isset($_POST["firstNameTextBox"]) && !empty($_POST["firstNameTextBox"]))
{
  $firstName = $_POST["firstNameTextBox"];
  $_SESSION["firstName"] = $_POST["firstNameTextBox"];
  $sessionFlag++;
}
else
{
    $message .= "Please enter first name</br>";
}

if (isset($_POST["lastNameTextBox"]) && !empty($_POST["lastNameTextBox"]))
{
  $lastName = $_POST["lastNameTextBox"];
  $_SESSION["lastName"] = $_POST["lastNameTextBox"];
  $sessionFlag++;
}
else
{
    $message .= "Please enter last name</br>";
}

if (isset($_POST["emailTextBox"]) && !empty($_POST["emailTextBox"]))
{
  $emailAddress = $_POST["emailTextBox"];
  $_SESSION["emailAddress"] = $_POST["emailTextBox"];
  $sessionFlag++;
}
else
{
    $message .= "Please enter email</br>";
}

if (isset($_POST["telephoneNumberTextBox"]) && !empty($_POST["telephoneNumberTextBox"]))
{
  $telephoneNumber = $_POST["telephoneNumberTextBox"];
  $_SESSION["telephoneNumber"] = $_POST["telephoneNumberTextBox"];
  $sessionFlag++;
}
else
{
    $message .= "Please enter telephone number</br>";
}

if (isset($_POST["SINTextBox"]) && !empty($_POST["SINTextBox"]))
{
  $SIN = $_POST["SINTextBox"];
  $_SESSION["SIN"] = $_POST["SINTextBox"];
  $sessionFlag++;
}
else
{
    $message .= "Please enter SIN</br>";
}

if (isset($_POST["passwordTextBox"]) && !empty($_POST["passwordTextBox"]))
{
  $password = $_POST["passwordTextBox"];
  $_SESSION["password"] = $_POST["passwordTextBox"];
  $sessionFlag++;
}
else
{
    $message .= "Please enter password</br>";
}

if ($sessionFlag == 6)
{
    $sessionFlag = true;
}
else
{
    $sessionFlag = false;
}

if ($sessionFlag)
{
  $connection = mysqli_connect($host, $username, $password, $database);
  if (!$connection)
  {
    die("Connection could not be established to database: " . $host);
  }
  mysqli_select_db($connection);

  $sqlquery = 
    "INSERT INTO `employee` (`FirstName`, `LastName`, `EmailAddress`, `TelephoneNumber`, `SocialInsuranceNumber`, `Password`) 
    VALUES ('$firstName', '$lastName', '$emailAddress', '$telephoneNumber', '$SIN', '$password')";
  mysqli_query($connection, $sqlquery);
  mysqli_close($connection);
  header("Location: ViewAllEmployees.php");
}
?>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create Account</title>
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
                require_once "Forms.php";
                if (!$sessionFlag)
                {
                    echo $message;
                }
                ?>
            </div>
        </div>
    </div>
    <?php
    include_once "Footer.php";
    ?>
</body>

</html>
