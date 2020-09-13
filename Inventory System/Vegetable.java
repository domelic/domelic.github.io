import java.util.Scanner;

/**
 * The {@code: Vegetable} class extends the {@code: FoodItem} class and has
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
public class Vegetable extends FoodItem {

  /**
   * The name of the farm supplier.
   */
  private String farmName;

  /**
   * Constructor of the {@code: Vegetable} class. Initializes farm name.
   */
  public Vegetable() {
    farmName = "";
  }

  /**
   * String to return the superclass item details and the childclass farm name.
   *
   * @return the food item details with the farm name
   */
  @Override
  public String toString() {
    return super.toString() + " farm supplier: " + farmName;
  }

  /**
   * Scanner to take input from the user. Take the string and assign it to the
   * farm name.
   * 
   * @param scanner input from keyboard
   * @return true if added
   */
  @Override
  public boolean addItem(Scanner scanner) {
    super.addItem(scanner);
    System.out.print("Enter the name of the farm supplier: ");
    farmName = scanner.nextLine();
    return true;
  }
}