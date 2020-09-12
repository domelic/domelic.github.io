import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * The <code>SavingsAccount</code> class is the subclass for the
 * <code>BankAccount</code> class. This class contains the interest rate and
 * minimum balance fields, and overrides the <code>toString()</code>,
 * <code>monthlyAccountUpdate()</code> and <code>addBanckAccount()</code> methods
 * from its parent class to figure in the method behaviors with the minimum
 * balance and interest rate fields.
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
public class SavingsAccount extends BankAccount {
  private double interestRate;
  private double minimumBalance;
  private DecimalFormat df = new DecimalFormat("###,###.00");

  /**
   * {@inheritDoc}
   * Also prints the minimum balance and interest rate values.
   *
   * @return concatenated account information with interest rate and minimum balance fields
   */
  @Override
  public String toString() {
    String print =
        super.toString()
        + "\nMinimum Balance: $" + df.format(minimumBalance)
        + "\nInterest Rate: 0" + df.format(interestRate);
    return print;
  }

  /**
   * {@inheritDoc}
   * Also prompts for the interest rate and minimum balance fields by calling
   * the class method to do so.
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
    super.addBankAccount(
        firstName, lastName, emailAddress, accountNumber, balance,
        minimumBalance, interestRate, monthlyFee);
    this.interestRate = interestRate.doubleValue();
    this.minimumBalance = minimumBalance.doubleValue();
  }

  /**
   * {@inheritDoc}
   * Adds interest to account's balance. If interest accrued exceeds available
   * balance, throws an error message.
   *
   * @return true if successfully updated, false otherwise
   */
  @Override
  public boolean monthlyAccountUpdate() {
    double tempValue = minimumBalance * (1 + interestRate);
    if (tempValue > balance) {
      return false;
    } else {
      minimumBalance = tempValue;
      return true;
    }
  }
}