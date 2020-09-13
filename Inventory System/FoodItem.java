import java.util.Scanner;
import java.util.NoSuchElementException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The {@code: FoodItem} class contains methods to take the item properties,
 * takes the item code, check if an item code of another {@code: FoodItem} is
 * equal to this one, update the item quantity and print out the item details.
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
public class FoodItem {
  /**
   * The unique code of the item.
   */
  private int itemCode;

  /**
   * The name of the item.
   */
  private String itemName;

  /**
   * The price of the item.
   */
  private float itemPrice;

  /**
   * The cost of the item.
   */
  private float itemCost;

  /**
   * Constructor to initialize the properties of the item.
   */
  public FoodItem() {
    itemCode = 0;
    itemName = "";
    itemPrice = 0.0f;
    itemCost = 0.0f;
  }

  /**
   * Prints the details of the item.
   */
  @Override
  public String toString() {
    return
      "Item: " + itemCode + " " + itemName + " price: $" + String.format("%.2f", itemPrice) 
      + " cost: $" + String.format("%.2f", itemCost);
  }

  public void printExpirySummary() {
    return;
  }

  public void removeExpiredItems(LocalDate today) {
    return;
  }

  /**
   * Compares the item codes of the food items and determines if one comes
   * before the other, and returns the appropriate integer value, 0 if equal,
   * 1 if greater than, -1 if less than.
   * 
   * @param temp the object that we're comparing to
   * @return 0 if equal, 1 if greater than, -1 if less than
   */
  // @Override
  // public int compareTo(Object temporary) {
  //   InventoryItem item = (InventoryItem) temporary;

  //   if (getItemCode() > item.getItemCode()) {
  //     return 1;
  //   } else if (getItemCode() < item.getItemCode()) {
  //     return -1;
  //   }
  //   return 0;
  // }

  /**
   * Checks if the code of an item is equal to the code of this item.
   * 
   * @param item the {@code: FoodItem} parameter to check
   * @return true if code is equal, false if not
   */
  public boolean isEqual(FoodItem item) {
    return item.itemCode == this.itemCode;
  }

  /**
   * Asks for user input and initializes the properties of the item.
   * Prompts until valid input is entered.
   * 
   * @param scanner input from the keyboard
   * @return true if item added, false if not
   */
  public boolean addItem(Scanner scanner) {
    boolean inputFlag = false;
    boolean tmpFlag = false;
    do {
      System.out.print("Enter the name for the item: ");
      itemName = scanner.nextLine();
      inputFlag = true;
      tmpFlag = false;
      do {
        System.out.print("Enter the cost of the item: ");
        try {
          itemCost = Float.parseFloat(scanner.nextLine().trim());
          if (itemCost < 0.0f) {
            throw new IllegalArgumentException("Invalid entry");
          }
          tmpFlag = true;
        } catch (NumberFormatException e) {
          System.err.println("Invalid entry\n");
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage() + "\n");
        }
      } while (!tmpFlag);
      tmpFlag = false;
      do {
        System.out.print("Enter the sales price of the item: ");
        try {
          itemPrice = Float.parseFloat(scanner.nextLine().trim());
          if (itemPrice < 0.0f) {
            throw new IllegalArgumentException("Invalid entry");
          }
          tmpFlag = true;
        } catch (NumberFormatException e) {
          System.err.println("Invalid entry\n");
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage() + "\n");
        }
      } while (!tmpFlag);
    } while (!inputFlag);
    return true;
  }

  /**
   * Returns the item code.
   * 
   * @return the item code 
   */
  public int getItemCode() {
    return itemCode;
  }

  /**
   * Asks for user input and initializes the code of the item.
   * Prompts until valid input is entered.
   * 
   * @param scanner input from the keyboard
   * @return true if code is valid, false if not
   */
  public boolean inputCode(Scanner scanner) {
    boolean inputFlag = false;
    do {
      System.out.print("Enter the code for the item: ");
      try {
        itemCode = Integer.parseInt(scanner.nextLine().trim());
        if (itemCode < 0) {
          throw new IllegalArgumentException("Invalid code");
        }
        inputFlag = true;
      } catch (NumberFormatException e) {
        System.err.println("Invalid code\n");
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\n");
      }
    } while (!inputFlag);
    return true;
  }
}