import java.lang.IllegalArgumentException;

/**
 * Class <code>TransactionIllegalArgumentException</code> extends IllegalArgumentException,
 * and is used for transaction types where an incorrect amount is entered for a given
 * account number, provided the transaction type (e.g., deposit or withdraw).
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
public class TransactionIllegalArgumentException extends IllegalArgumentException {

  private double amount;
  private long accountNumber;
  private String type;

  /**
   * Constructor to call the super() property for empty paragraph list.
   */
  public TransactionIllegalArgumentException() {
    super();

  }

  /**
   * Constructor to set instance variables from the argument list.
   *
   * @param amount the amount to update the balance
   * @param accountNumber the client's account number
   * @param type a String indicating the type of transaction
   */
  public TransactionIllegalArgumentException(double amount, long accountNumber, String type) {
    this.amount = amount;
    this.accountNumber = accountNumber;
    this.type = type;
  }

  /**
   * A getter for the amount.
   *
   * @return the amount that's used
   */
  public double getAmount() {
    return amount;
  }

  /**
   * A getter for the account number.
   *
   * @return the client's account number
   */
  public double getAccountNumber() {
    return accountNumber;
  }

  /**
   * A getter for the transaction type.
   *
   * @return a String of the transaction type
   */
  public String getType() {
    return type;
  }

  /**
   * Builds the error message from the instance variables indicating the
   * type of transaction performed, and the amount for the account number.
   *
   * @return a String detailing the error message
   */
  public String toString() {
    return String.format("Unable to %s the amount of %.2f %s account %d.",
      type, amount, type.equals("deposit") ? "to" : "from", accountNumber);
  }
}