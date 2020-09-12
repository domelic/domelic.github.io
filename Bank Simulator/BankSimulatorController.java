import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the "BankSimulator.fxml". Declares labels, text fields, text areas and buttons.
 * Contains methods to create an account, display an account, update the balance or read from a
 * text file and read to a text file, as well as to quit the GUI application.
 *
 * <br><ul><li>ID: 040918352</li>
 * <li>Course: CST8132 305</li>
 * <li>Assignment: 9</li>
 * <li>Professor: Md.Istiaque Shariar</li>
 * <li>Date: April 19, 2019</li></ul>
 *
 * @author Damir Omelic
 * @since 11.0.2
 * @version 1.0
 */
public class BankSimulatorController {

  private String accountType = "0";
  private String updateBalance = "0";
  private Bank bank = new Bank();

  @FXML
  private Label addAccountNumberLabel;

  @FXML
  private Label firstNameLabel;

  @FXML
  private Label lastNameLabel;

  @FXML
  private Label phoneNumberLabel;

  @FXML
  private Label emailAddressLabel;

  @FXML
  private Label balanceLabel;

  @FXML
  private Label minimumBalanceLabel;

  @FXML
  private Label interestRateLabel;

  @FXML
  private Label monthlyFeeLabel;

  @FXML
  private Label createAccountLabel;

  @FXML
  private Label displayAccountNumberLabel;

  @FXML
  private Label updatedAccountsLabel;

  @FXML
  private Label updateAccountNumberLabel;

  @FXML
  private Label amountLabel;

  @FXML
  private Label pathToRecordsLabel;

  @FXML
  private Label readRecordsLabel;

  @FXML
  private Label writeRecordsLabel;

  @FXML
  private Label updateBalanceLabel;

  @FXML
  private TextField phoneNumberTextField;

  @FXML
  private TextField pathToRecordsTextField;

  @FXML
  private TextField interestRateTextField;

  @FXML
  private TextField lastNameTextField;

  @FXML
  private TextField amountTextField;

  @FXML
  private TextField firstNameTextField;

  @FXML
  private TextField displayAccountNumberTextField;

  @FXML
  private TextField updateAccountNumberTextField;

  @FXML
  private TextArea consoleTextArea;

  @FXML
  private TextField balanceTextField;

  @FXML
  private TextField monthlyFeeTextField;

  @FXML
  private TextField minimumBalanceTextField;

  @FXML
  private TextField addAccountNumberTextField;

  @FXML
  private TextField emailAddressTextField;

  @FXML
  private Button quitButton;

  /**
   * Quits the stage.
   *
   * @param event the click event
   */
  @FXML
  void quitButtonPressed(ActionEvent event) {
    Stage stage = (Stage) quitButton.getScene().getWindow();
    stage.close();
  }

  /**
   * Sets the account type and disables non-applicable fields.
   *
   * @param event the click event
   */
  @FXML
  void chequingRadioButtonPressed(ActionEvent event) {
    accountType = "Chequing";
    minimumBalanceTextField.setEditable(false);
    interestRateTextField.setEditable(false);
    monthlyFeeTextField.setEditable(true);
    minimumBalanceTextField.setText("");
    interestRateTextField.setText("");
  }

  /**
   * Sets the account type and disables non-applicable fields.
   *
   * @param event the click event
   */ 
  @FXML
  void savingsRadioButtonPressed(ActionEvent event) {
    accountType = "Savings";
    minimumBalanceTextField.setEditable(true);
    interestRateTextField.setEditable(true);
    monthlyFeeTextField.setEditable(false);
    monthlyFeeTextField.setText("");
  }

  /**
   * Creates the account from user input. Has exception handling for each text field.
   *
   * @param event the click event
   * @throws IndexOutOfBoundsException if account type not chosen
   * @throws NumberFormatException if no input or invalid input entered
   */
  @FXML
  void createAccountButtonPressed(ActionEvent event) {
    try {
      if (accountType == "0") {
        throw new IndexOutOfBoundsException("Select Account Type");
      } else {
        createAccountLabel.setText("");
      }

      long accountNumber = 0L;
      String firstName = "";
      String lastName = "";
      long phoneNumber = 0L;
      String emailAddress = "";
      BigDecimal balance = BigDecimal.ZERO;
      BigDecimal minimumBalance = BigDecimal.ZERO;
      BigDecimal monthlyFee = BigDecimal.ZERO;
      BigDecimal interestRate = BigDecimal.ZERO;
      boolean errorFlag = false;

      try {
        accountNumber = Long.parseLong(addAccountNumberTextField.getText());
        if (accountNumber < 0L) {
          throw new NumberFormatException();
        }
        addAccountNumberLabel.setText("");
      } catch (NumberFormatException accountNumberException) {
        errorFlag = true;
        addAccountNumberLabel.setText("Enter valid account number");
      }


      try {
        firstName = firstNameTextField.getText();
        if (firstName.length() > 35 || !firstName.matches("[a-zA-Z ]+")) {
          throw new NumberFormatException();
        }
        firstNameLabel.setText("");
      } catch (NumberFormatException firstNameException) {
        errorFlag = true;
        firstNameLabel.setText("Enter valid first name");
      }

      try {
        lastName = lastNameTextField.getText();
        if (lastName.length() > 35 || !lastName.matches("[a-zA-Z ]+")) {
          throw new NumberFormatException();
        }
        lastNameLabel.setText("");
      } catch (NumberFormatException lastNameException) {
        errorFlag = true;
        lastNameLabel.setText("Enter valid last name");
      }

      try {
        phoneNumber = Long.parseLong(phoneNumberTextField.getText());
        if (phoneNumber < 0L) {
          throw new NumberFormatException();
        }
        phoneNumberLabel.setText("");
      } catch (NumberFormatException phoneNumberException) {
        errorFlag = true;
        phoneNumberLabel.setText("Enter valid phone number");
      }

      try {
        emailAddress = emailAddressTextField.getText();
        if (emailAddress.length() > 255 || ! emailAddress.matches(
              "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
          emailAddressLabel.setText("Enter valid email address");
          throw new NumberFormatException();
        }
        emailAddressLabel.setText("");
      } catch (NumberFormatException emailAddressException) {
        errorFlag = true;
        emailAddressLabel.setText("Enter valid email address");
      }

      try {
        balance = new BigDecimal(balanceTextField.getText());
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
          throw new NumberFormatException();
        }
        balanceLabel.setText("");
      } catch (NumberFormatException balanceException) {
        errorFlag = true;
        balanceLabel.setText("Enter valid balance");
      }

      if (accountType == "Chequing") {
        minimumBalanceLabel.setText("");
        interestRateLabel.setText("");
        try {
          monthlyFee = new BigDecimal(monthlyFeeTextField.getText());
          if (monthlyFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new NumberFormatException();
          }
          monthlyFeeLabel.setText("");
        } catch (NumberFormatException monthlyFeeException) {
          errorFlag = true;
          monthlyFeeLabel.setText("Enter valid monthly fee");
        }
      }

      if (accountType == "Savings") {
        monthlyFeeLabel.setText("");
        try {
          minimumBalance = new BigDecimal(minimumBalanceTextField.getText());
          if (minimumBalance.compareTo(balance) > 0) {
            throw new NumberFormatException();
          }
          minimumBalanceLabel.setText("");
        } catch (NumberFormatException | NullPointerException minimumBalanceException) {
          errorFlag = true;
          minimumBalanceLabel.setText("Enter valid minimum balance less than balance");
        }

        try {
          interestRate = new BigDecimal(interestRateTextField.getText());
          if (interestRate.compareTo(BigDecimal.ZERO) < 0
              || interestRate.compareTo(BigDecimal.ONE) > 0) {
            throw new NumberFormatException();
          }
          interestRateLabel.setText("");
        } catch (NumberFormatException | NullPointerException interestRateException) {
          errorFlag = true;
          interestRateLabel.setText("Enter valid interest rate (0,1)");
        }
      }

      if (errorFlag) {
        throw new NumberFormatException();
      }

      if (bank.addAccount(
          accountType, firstName, lastName, emailAddress, accountNumber, 
          balance, minimumBalance, interestRate, monthlyFee)) {
        createAccountLabel.setText("Account Created");
      } else {
        addAccountNumberLabel.setText("Enter valid account number");
        throw new NumberFormatException();        
      }
    } catch (NumberFormatException e) {
      createAccountLabel.setText("Account Not Created");
    } catch (IndexOutOfBoundsException e) {
      createAccountLabel.setText(e.getMessage());
    }
  }

  /**
   * Shows the account information for the particular account.
   * 
   * @param event the click event
   * @throws NumberFormatException if no input or invalid input entered
   */
  @FXML
  void showAccountButtonPressed(ActionEvent event) {
    try {
      long accountNumber = Long.parseLong(displayAccountNumberTextField.getText());
      displayAccountNumberLabel.setText("");
      if (bank.displayAccount(accountNumber).equals("-1")) {
        throw new NumberFormatException();
      }
      consoleTextArea.setText(bank.displayAccount(accountNumber));
    } catch (NumberFormatException e) {
      displayAccountNumberLabel.setText("Enter valid account number");
    }
  }

  /**
   * Shows the account information for all accounts.
   *
   * @param event the click event
   */
  @FXML
  void showAllAccountsButtonPressed(ActionEvent event) {
    consoleTextArea.setText(bank.printAccountDetails());
  }

  /**
   * Runs the monthly update on all accounts and displays the message.
   * Displays failed if even a single account failed updating, but otherwise
   * updates the rest of the accounts (if possible) regardless.
   *
   * @param event the click event
   */
  @FXML
  void runMonthlyUpdateButtonPressed(ActionEvent event) {
    if (bank.monthlyUpdate()) {
      updatedAccountsLabel.setText("Monthly Update Success");
    } else {
      updatedAccountsLabel.setText("Monthly Update Failed");
    }
  }

  /**
   * Updates the balance for an account number, either deposit or withdraw.
   *
   * @throws IllegalArgumentException if radio button not chosen
   * @throws IndexOutOfBoundsException if no input entered or radio button
   * @throws NumberFormatException if invalid input entered
   */
  @FXML
  void updateBalanceButtonPressed(ActionEvent event) {
    try {
      if (updateBalance.equals("0")) {
        throw new IllegalArgumentException("Select Radio Button");
      }
      updateBalanceLabel.setText("");
      Long accountNumber = Long.parseLong(updateAccountNumberTextField.getText());
      if (accountNumber.compareTo(0L) < 0) {
        throw new NumberFormatException();
      }
      BigDecimal amount = new BigDecimal(amountTextField.getText());
      if (amount.compareTo(BigDecimal.ZERO) < 0) {
        throw new NumberFormatException();
      }
      switch (updateBalance) {
        case "Deposit":
          if (!bank.deposit(accountNumber, amount)) {
            updateBalanceLabel.setText("Invalid amount");
          } else {
            updateAccountNumberLabel.setText("");
            amountLabel.setText("");
            updateBalanceLabel.setText("Successfully Deposited");
          }
          break;
        case "Withdraw":
          if (!bank.withdraw(accountNumber, amount)) {
            updateBalanceLabel.setText("Insufficient Funds");
          } else {
            updateAccountNumberLabel.setText("");
            amountLabel.setText("");
            updateBalanceLabel.setText("Successfully Withdrawn");
          }
          break;
        default:
          break;
      }
    } catch (NumberFormatException e) {
      updateAccountNumberLabel.setText("Enter valid account number");
      amountLabel.setText("Enter valid amount");
    } catch (IndexOutOfBoundsException e) {
      updateBalanceLabel.setText("");
    } catch (IllegalArgumentException e) {
      updateBalanceLabel.setText(e.getMessage());
    }
  }

  /**
   * Sets the update balance choice.
   *
   * @param event the click event
   */ 
  @FXML
  void depositRadioButtonPressed(ActionEvent event) {
    updateBalance = "Deposit";
  }

  /**
   * Sets the update balance choice.
   *
   * @param event the click event
   */
  @FXML
  void withdrawRadioButtonPressed(ActionEvent event) {
    updateBalance = "Withdraw";
  }

  /**
   * Reads records from a file given the file path.
   *
   * @param event the click event
   */
  @FXML
  void readRecordsButtonPressed(ActionEvent event) {
    if (bank.openInputFile(pathToRecordsTextField.getText())) {
      if (bank.readRecords()) {
        pathToRecordsLabel.setText("");
        readRecordsLabel.setText("Records Read From File");
      } else {
        pathToRecordsLabel.setText("Records Malformed");
      }
      bank.closeInputFile();
    } else {
      pathToRecordsLabel.setText("Error Opening File");
    }
  }

  /**
   * Writes records stored in the array to a textual file.
   *
   * @param event the click event
   */
  @FXML
  void writeRecordsButtonPressed(ActionEvent event) {
    if (bank.openOutputFile()) {
      bank.writeOutputFile();
      bank.closeOutputFile();
      writeRecordsLabel.setText("Wrote Records to File");
    } else {
      writeRecordsLabel.setText("Error Writing File");
    }
  }
}
