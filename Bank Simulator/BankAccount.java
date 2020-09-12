import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * The <code>BankAccount</code> method is the superclass for the account types.
 * This class contains account numbers and balances, and references the client
 * information.
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
public abstract class BankAccount {
  protected long accountNumber;
  protected Person accHolder;
  protected double balance;

  private DecimalFormat df = new DecimalFormat("###,###.00");

  /**
   * Returns a String representation of the account holder's information.
   * Contains the client's name, email address and balance.
   * 
   * @return concatenated account information
   */
  @Override
  public String toString() {
    return
        "Name: " + accHolder.getFirstName() + " " + accHolder.getLastName()
        + "\nAccount Number: " + accountNumber
        + "\nEmail Address: " + accHolder.getEmailAddress()
        + "\nBalance: $" + df.format(balance);
  }

  /**
   * Instantiates the account holder by passing the <code>prompt(int)</code> method
   * with each option value to the <code>Person</code> constructor. Also initializes
   * the balance with the same method.
   *
   * @param firstName first name
   * @param lastName last name
   * @param emailAddress email address
   * @param accountNumber account number
   * @param balance balance
   * @param minimumBalance minimum balance
   * @param interestRate interest rate
   * @param monthlyFee monthly fee
   */
  public void addBankAccount(String firstName, String lastName, 
      String emailAddress, long accountNumber, BigDecimal balance,
      BigDecimal minimumBalance, BigDecimal interestRate, BigDecimal monthlyFee) {
    accHolder = new Person(firstName, lastName, emailAddress);
    this.accountNumber = accountNumber;
    this.balance = balance.doubleValue();
  }

  /**
   * Deposits a positive amount to the account holder's balance.
   *
   * @param amount the amount to deposit
   * @return true if successfully deposited, false otherwise
   * @throws TransactionIllegalArgumentException if amount is negative
   */
  public boolean deposit(double amount) {
    try {
      if (amount < 0) {
        throw new TransactionIllegalArgumentException(
          amount, accountNumber, "deposit");
      }
      balance += amount;
      return true;
    } catch (TransactionIllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Withdraws a positive amount from the account holder's balance.
   *
   * @param amount the amount to withdraw
   * @return true if successfully deposited, false otherwise
   * @throws TransactionIllegalArgumentException if amount is greater than balance
   *     or is negative
   */
  public boolean withdraw(double amount) {
    try {
      if (amount > balance || amount < 0) {
        throw new TransactionIllegalArgumentException(
          amount, accountNumber, "withdraw");
      }
      balance -= amount;
      return true;
    } catch (TransactionIllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Updates the monthly account balance.
   */
  public abstract boolean monthlyAccountUpdate();
}