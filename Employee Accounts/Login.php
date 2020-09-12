<?php
require_once "MySQLConnectionInfo.php";
session_start();

$message = "</br>";

$sessionFlag = 0;

$emailAddress = "";
$password = "";

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

if ($sessionFlag == 2)
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
  $sqlquery = "SELECT * FROM employee";
  $result = mysqli_query($connection, $sqlquery);
  for ($i = 0; $i < mysqli_num_rows($result); $i++)
  {
      $rows = mysqli_fetch_row($result);
      if (strcmp($emailAddress, $rows[3]) == 0 && strcmp($password, $rows[6]) == 0)
      {
        $_SESSION["firstName"] = $rows[1];
        $_SESSION["lastName"] = $rows[2];
        $_SESSION["telephoneNumber"] = $rows[4];
        $_SESSION["SIN"] = $rows[5];
        header("Location: ViewAllEmployees.php");
      }
  }
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Login</title>
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
            <form method="post">
                    <fieldset>
                        <legend>Login</legend>
                        <p>
                            <label>Email Address</label></br>
                            <input type="email" name="emailTextBox"/>
                        </p>
                        <p>
                            <label>Password</label></br>
                            <input type="password" name="passwordTextBox"/>
                        </p>
                    </fieldset>
                    </fieldset></br>
                    <input type="submit" name="loginButton" value="Login"/>&nbsp;
                    <input type="reset"/>
                </form>
            <?php
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