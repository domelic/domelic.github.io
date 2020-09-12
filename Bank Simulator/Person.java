/**
 * The <code>Person</code> class contains the client's personal data, with
 * fields for first and last name and email address and their
 * respective getter methods.
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
public class Person {
  
  private String firstName;
  private String lastName;
  private String emailAddress;

  /**
   * Constructor for the <code>Person</code> class to initalize the client's
   * name and email address fields.
   *
   * @param firstName the client's first name
   * @param lastName the client's last name
   * @param emailAddress the client's email address
   */
  public Person(
      String firstName, String lastName, String emailAddress) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
  }

  /**
   * Getter method for the client's first name.
   *
   * @return first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Getter method for the client's last name.
   *
   * @return last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Getter method for the client's email address.
   *
   * @return email address
   */
  public String getEmailAddress() {
    return emailAddress;
  }
}