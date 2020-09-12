import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.IllegalStateException;
import java.lang.SecurityException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The <code>Bank</code> class implements the bank menu options. The methods
 * are to add, update, display and calculate accounts. This is the shadow class
 * of the <code>Assign1</code> class. This class serves to store all accounts in
 * an array, and to define its maximum allowable size.
 *
 * <p>NOTE: If an entry already exists, and the user changes a field other than the
 * account number in the input file and attempts to read it again, an error message
 * is displayed on-screen as the application does not allow the update, that is, 
 * to add an account (in the case of the application's requimerents) for an entry
 * with the same account number, which is treated as a primary key. However, due
 * to the change in the application's requirements and the removal and change of
 * certain fields and methods, this was no longer possible unless the application
 * is reworked to allow for it. For proof, please see lab 6 where this restriction is
 * applied in the application.
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
public class Bank {
  private List<BankAccount> accounts;
  private int maxSize;
  private int numAccounts;
  private Scanner input;
  private Formatter output;

  /**
   * Constructor for the <code>Bank</code> class to initialize a default
   * max number of allowable accounts by calling the parametrized constructor
   * with a set value.
   */
  public Bank() {
    this(1000);
  }

  /**
   * Constructor for the <code>Bank</code> class to initialize an arbitrary
   * number of allowable accounts and to initialize the array with that number.
   *
   * @param size the number of allowable accounts
   */  
  public Bank(int size) {
    input = new Scanner(System.in);
    numAccounts = 0;
    maxSize = size;
    accounts = new ArrayList<BankAccount>();
  }

  /**
   * Opens the given file in a new Scanner by getting its path.
   *
   * @param pathToRecords path to the file records
   * @return true if successfully opened, false otherwise
   * @throws IOException if path is non existing
   */
  public boolean openInputFile(String pathToRecords) {
    input = new Scanner(System.in);
    try {
      input = new Scanner(Paths.get(pathToRecords));
      return true;
    } catch (IOException ioException) {
      return false;
      //System.err.println("Error opening file. Terminating.");
    }
  }

  /**
   * Adds records from a file in provided format to the accounts array.
   * 
   * @return true if successfully read record, false otherwise
   * @throws NoSuchElementException if file is improperly formatted
   * @throws IllegalStateException if file cannot be read
   * @throws IllegalArgumentException if field data is invalid
   */
  public boolean readRecords() {
    try {
      while (input.hasNext()) {
        ++numAccounts;
        String type = input.next();
        if (type.equals("c") || type.equals("C")) {
          accounts.add(new ChequingAccount());
        } else if (type.equals("s") || type.equals("S")) {
          accounts.add(new SavingsAccount());
        } else {
          throw new IllegalArgumentException();
        }

        int accountNumber = input.nextInt();

        if (accountNumber < 0) {
          throw new IllegalArgumentException();
        }

        String firstName = input.next();

        if (firstName.length() > 35 || !firstName.matches("[a-zA-Z ]+")) {
          throw new IllegalArgumentException();
        }

        String lastName = input.next();


        if (lastName.length() > 35 || !lastName.matches("[a-zA-Z ]+")) {
          throw new IllegalArgumentException();
        }

        long phoneNumber = input.nextLong();

        if (phoneNumber < 0L) {
          throw new IllegalArgumentException();
        }

        String emailAddress = input.next();

        if (emailAddress.length() > 255 || ! emailAddress.matches(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
          throw new IllegalArgumentException();
        }

        BigDecimal balance = new BigDecimal(input.nextDouble());

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
          throw new IllegalArgumentException();
        }

        BigDecimal monthlyFee = BigDecimal.ZERO;

        if (type.equals("c") || type.equals("C")) {
          monthlyFee = new BigDecimal(input.nextDouble());

          if (monthlyFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
          }
        }

        BigDecimal interestRate = BigDecimal.ZERO;
        BigDecimal minimumBalance = BigDecimal.ZERO;

        if (type.equals("s") || type.equals("S")) {
          interestRate = new BigDecimal(input.nextDouble());
          minimumBalance = new BigDecimal(input.nextDouble());

          if (interestRate.compareTo(BigDecimal.ZERO) < 0
              || interestRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException();
          }

          if (minimumBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
          }
        }

        // check if account number already exists
        for (int i = 0; i < accounts.size(); i++) {
          try {
            if (i != numAccounts - 1) {
              if (accountNumber == accounts.get(i).accountNumber) {
                throw new IllegalArgumentException();
              } // iterate account numbers to repeat prompt if existing entered
            }
          } catch (NullPointerException e) {
            continue;
          } // ignore uninitialized indexes
        }

        accounts.get(numAccounts - 1).addBankAccount(
            firstName, lastName, emailAddress, accountNumber, balance, 
            minimumBalance, interestRate, monthlyFee);
      }
    } catch (NoSuchElementException elementException) {
      accounts.remove(accounts.size() - 1);
      --numAccounts;
      return false;
      //System.err.println("File improperly formed. Terminating.");
    } catch (IllegalStateException stateException) {
      accounts.remove(accounts.size() - 1);
      --numAccounts;
      return false;
      //System.err.println("Error reading from file. Terminating.");
    } catch (IllegalArgumentException e) {
      accounts.remove(accounts.size() - 1);
      --numAccounts;
      //System.err.println(e.getMessage());
      return false;
    }
    return true;
  }

  /**
   * Closes the Scanner for the input file.
   */
  public void closeInputFile() {
    if (input != null) {
      input.close();
    }
  }

  /**
   * Opens a new Formatter for outputting to a new file.
   *
   * @return true if successfully opened file, false otherwise
   * @throws SecurityException if user has no write permission
   * @throws FileNotFoundException if new file cannot be opened
   */
  public boolean openOutputFile() {
    try {
      output = new Formatter("bankoutput.txt");
      return true;
    } catch (SecurityException securityException) {
      return false;
      //System.err.println("Write permission denied. Terminating.");
    } catch (FileNotFoundException fileNotFoundException) {
      return false;
      //System.err.println("Error opening file. Terminating.");
    }
  }

  /**
   * Writes account line information to the output file.
   */
  public void writeOutputFile() {
    String str = String.format(
        "Banking System\n"
        + "*****************************\n"
        + "Number of Account holders: " + accounts.size()) + "\n";
    output.format(str);

    for (int i = 0; i < accounts.size(); i++) {
      String type = " ";
      try {
        if (accounts.get(i) instanceof ChequingAccount) {
          type = "Chequing";
        }
        if (accounts.get(i) instanceof SavingsAccount) {
          type = "Savings";
        }
        String info = "\n" + type + "\n" + accounts.get(i) + "\n";
        output.format("\n" + info);
      } catch (NullPointerException e) {
        continue;
      }
    } // iterates through the accounts array
  }

  /**
   * Closes the Formatter for the output file.
   */
  public void closeOutputFile() {
    if (output != null) {
      output.close();
    }
  }

  /**
   * Method to add an account to the account array. Allowable size is ignored
   * as the account array is stored then retreived from an array of size + 1.
   * Prompts for correct account type choice, instantiates the array index with
   * the chosen sub-type, then asks for an account number and checks if it's
   * already existing (prompts again if yes). Finally, accesses the
   * <code>addBankAccount()</code> method of the <code>BankAccount</code>
   * class variable to continue prompting and returns.
   *
   * @param firstName first name
   * @param lastName last name
   * @param emailAddress email address
   * @param accountNumber account number
   * @param balance balance
   * @param minimumBalance minimum balance
   * @param interestRate interest rate
   * @param monthlyFee monthly fee
   * @return true if executed correctly
   * @throws StringIndexOutOfBoundsException if whitespace is entered
   * @throws IllegalArgumentException if input does not match valid options
   * @throws NullPointerException if accessing uninitialized index
   */
  public boolean addAccount(String accountType, String firstName, String lastName,
       String emailAddress, long accountNumber, BigDecimal balance, 
       BigDecimal minimumBalance, BigDecimal interestRate, BigDecimal monthlyFee) {
    ++numAccounts;
    switch (accountType) {
      case "Chequing":
        accounts.add(new ChequingAccount());
        break;
      case "Savings":
        accounts.add(new SavingsAccount());
        break;
      default:
        break;
    } // initialize array index with account type

    // access accounts member method to continue prompting

    // check if account number already exists
    for (int i = 0; i < accounts.size(); i++) {
      try {
        if (i != numAccounts - 1) {
          if (accountNumber == accounts.get(i).accountNumber) {
            accounts.remove(accounts.size() - 1);
            --numAccounts;
            return false;
          } // iterate account numbers to repeat prompt if existing entered
        }
      } catch (NullPointerException e) {
        continue;
      } // ignore uninitialized indexes
    }

    accounts.get(numAccounts - 1).addBankAccount(
        firstName, lastName, emailAddress, accountNumber, balance,
        minimumBalance, interestRate, monthlyFee);

    return true;
  }

  /**
   * Method to return a single account's details. Calls the <code>findAccount()</code>
   * method to prompt for an account number. If existing, returns the String
   * representation for that account, otherwise returns a message.
   *
   * @return account details as a String
   * @throws ArrayIndexOutOfBoundsException if account does not exist
   */
  public String displayAccount(long accountNumber) {
    try {
      int index = findAccount(accountNumber);
      String type = " ";
      if (accounts.get(index) instanceof ChequingAccount) {
        type = "Chequing";
      }
      if (accounts.get(index) instanceof SavingsAccount) {
        type = "Savings"; 
      }
      return type + "\n" + accounts.get(index).toString();      
    } catch (IndexOutOfBoundsException e) {
      return "-1";
    }
  }

  /**
   * Method to print all account details by looping through the accounts array.
   * Each account field is printed to a new line.
   *
   * @return account details as a string
   */
  public String printAccountDetails() {
    //openOutputFile();
    String str = String.format(
        "Banking System\n"
        + "*****************************\n"
        + "Number of Account holders: " + accounts.size()) + "\n";
    //output.format(header);

    for (int i = 0; i < accounts.size(); i++) {
      String type = " ";
      try {
        if (accounts.get(i) instanceof ChequingAccount) {
          type = "Chequing";
        }
        if (accounts.get(i) instanceof SavingsAccount) {
          type = "Savings";
        }
        str += "\n" + type + "\n" + accounts.get(i) + "\n";
        //output.format("\n" + info);
      } catch (NullPointerException e) {
        continue;
      }
    } // iterates through the accounts array
    //closeOutputFile();
    return str;
  }

  /**
   * Method to deposit an amount from an account's balance,
   * provided the correct account number is given.
   *
   * @param accountNumber account number
   * @param amount amount
   */
  public boolean deposit(long accountNumber, BigDecimal amount) {
    return accounts.get(findAccount(accountNumber)).deposit(amount.doubleValue());
  }

  /**
   * Method to withdraw an amount from an account's balance,
   * provided the correct account number is given.
   *
   * @param accountNumber account number
   * @param amount amount
   */
  public boolean withdraw(long accountNumber, BigDecimal amount) {
    return accounts.get(findAccount(accountNumber)).withdraw(amount.doubleValue());
  }

  /**
   * Method to prompt for an account number and return its index in the
   * accounts array. If not found, returns -1.
   *
   * @param number number
   * @return index of the account number if found, otherwise -1
   */
  public int findAccount(long number) {
    for (int i = 0; i < accounts.size(); i++) {
      if (accounts.get(i).accountNumber == number) {
        return i;
      } // returns index
    }
    return -1;
  }

  /**
   * Method to access the <code>monthylAccountUpdate()</code> method
   * by iterating through the accounts array. This method is implemented
   * in the subclasses of the accounts member.
   *
   * @return true if successfuly updated, false otherwise
   */
  public boolean monthlyUpdate() {
    boolean updateFlag = true;
    for (int i = 0; i < accounts.size(); i++) {
      updateFlag = accounts.get(i).monthlyAccountUpdate();
    }
    return updateFlag;
  }
}
