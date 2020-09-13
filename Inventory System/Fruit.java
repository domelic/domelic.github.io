import java.util.Scanner;

/**
 * The {@code: Fruit} class extends the {@code: FoodItem} class and has
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
public class Fruit extends FoodItem {

  /**
   * The name of the orchard supplier.
   */
  private String orchardName;

  /**
   * Constructor of the {@code: Fruit} class. Initializes orchard name.
   */
  public Fruit() {
    orchardName = "";
  }

  /**
   * String to return the superclass item details and the childclass orchard name.
   *
   * @return the food item details with the orchard name
   */
  @Override
  public String toString() {
    return super.toString() + " orchard supplier: " + orchardName;
  }

  /**
   * Scanner to take input from the user. Take the string and assign it to the
   * orchard name.
   * 
   * @param scanner input from keyboard
   * @return true if added
   */
  @Override
  public boolean addItem(Scanner scanner) {
    super.addItem(scanner);
    System.out.print("Enter the name of the orchard supplier: ");
    orchardName = scanner.nextLine();
    return true;
  }
}