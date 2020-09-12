import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * The <code>ChequingAccount</code> class is the subclass for the
 * <code>BankAccount</code> class. This class contains the monthly fee field,
 * and overrides the <code>toString()</code>, <code>monthlyAccountUpdate()</code>
 * and <code>addBankAccount()</code> methods from its parent class to figure in
 * the method behaviors with the monthly fee field.
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
public class ChequingAccount extends BankAccount {
  private double monthlyFee;
  private DecimalFormat df = new DecimalFormat("###,###.00");

  /**
   * {@inheritDoc}
   * Also returns the monthly fee value.
   *
   * @return concacenated account information with monthly fee field 
   */
  @Override
  public String toString() {
    String print = 
        super.toString()
        + "\nMonthly Fee: $" + df.format(this.monthlyFee);
    return print;
  }

  /**
   * {@inheritDoc}
   * Also prompts for the monthly fee field.
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
  @Override
  public void addBankAccount(String firstName, String lastName, 
      String emailAddress, long accountNumber, BigDecimal balance,
      BigDecimal minimumBalance, BigDecimal interestRate, BigDecimal monthlyFee) {
    super.addBankAccount(
        firstName, lastName, emailAddress, accountNumber, balance,
        minimumBalance, interestRate, monthlyFee);
    this.monthlyFee = monthlyFee.doubleValue();
  }

  /**
   * {@inheritDoc}
   * Deducts the monthly fee from the account's balance. If the reduced amount
   * is negative, displays an error message.
   *
   * @return true if account updated, false otherwise
   */
  @Override
  public boolean monthlyAccountUpdate() {
    if (monthlyFee > balance) {
      return false;
    } else {
      balance -= monthlyFee;
      return true;
    }
  }
}