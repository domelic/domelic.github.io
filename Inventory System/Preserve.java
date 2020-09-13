import java.util.Scanner;

/**
 * The {@code: Preserve} class extends the {@code: FoodItem} class and has
 * methods to print the object properties and to take an input for the
 * instance variable(s).
 * 
 * <ul>
 * <li>Student Number: 040918352</li>
 * <li>Course: CST8130 - Data Structures</li>
 * <li>Assignment: 3</li>
 * <li>Professor: James Mwangi</li>
 * </ul>
 *
 * @author Damir Omelic
 * @version 1.0
 * @since 2020-06-04
 */
public class Preserve extends FoodItem {

  /**
   * The size of the jar in mililiters.
   */
  private int jarSize;
  
  /**
   * Constructor of the {@code: Preserve} class. Initializes the jar size.
   */
  public Preserve() {
    jarSize = 0;
  }

  /**
   * String to return the superclass item details and the childclass jar size.
   *
   * @return the food item details with the jar size
   */
  @Override
  public String toString() {
    return super.toString() + " size: " + jarSize + "mL";
  }

  /**
   * Scanner to take input from the user. Take the input and assign it to the
   * jar size if valid.
   * 
   * @param scanner input from keyboard
   * @return true if added
   */
  @Override
  public boolean addItem(Scanner scanner) {
    super.addItem(scanner);
    boolean inputFlag = false;
    do {
      System.out.print("Enter the size of the jar in mililitres: ");
      try {
        jarSize = Integer.parseInt(scanner.nextLine().trim());
        if (jarSize < 0) {
          throw new IllegalArgumentException("Invalid entry");
        }
        inputFlag = true;
      } catch (NumberFormatException e) {
        System.err.println("Invalid entry\n");
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\n");
      }
    } while (!inputFlag);
    return true;
  }
}