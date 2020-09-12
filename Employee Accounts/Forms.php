<?php
if (isset($_POST["firstNameTextBox"]))
{
  $firstName = $_POST["firstNameTextBox"];
}
else
{
  $firstName = "Not set";
}

if (isset($_POST["lastNameTextBox"]))
{
  $lastName = $_POST["lastNameTextBox"];
}
else
{
  $lastName = "Not set";
}

if (isset($_POST["emailTextBox"]))
{
  $email = $_POST["emailTextBox"];
}
else
{
  $email = "Not set";
}

if (isset($_POST["telephoneNumberTextBox"]))
{
  $telephoneNumber = $_POST["telephoneNumberTextBox"];
}
else
{
  $telephoneNumber = "Not set";
}

if (isset($_POST["SINTextBox"]))
{
  $SIN = $_POST["SINTextBox"];
}
else
{
  $SIN = "Not set";
}

if (isset($_POST["passwordTextBox"]))
{
  $password = $_POST["passwordTextBox"];
}
else
{
  $password = "Not set";
}

$message .= <<<_END
<form method="post">
  <fieldset>
    <legend>Create your new account</legend>
    <p>
      Please fill in all information.
    </p>
    <p>
      <label>First Name</label></br>
      <input type="text" name="firstNameTextBox"/>
    </p>
    <p>
      <label>Last Name</label></br>
      <input type="text" name="lastNameTextBox"/>
    </p>
    <p>
      <label>Email Address</label></br>
      <input type="email" name="emailTextBox"/>
    </p>
    <p>
      <label>Telephone Number</label></br>
      <input type="number" name="telephoneNumberTextBox"/>
    </p>
    <p>
      <label>SIN</label></br>
      <input type="number" name="SINTextBox"/>
    </p>
    <p>
      <label>Password</label></br>
      <input type="password" name="passwordTextBox"/>
    </p>
  </fieldset>
  </fieldset></br>
    <input type="submit" name="submitButton" value="Submit Information"/>&nbsp;
    <input type="reset"/>
</form>
_END;
?>
