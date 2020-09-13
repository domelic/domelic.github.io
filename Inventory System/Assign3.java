import java.util.NoSuchElementException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The {@code: Assign3} class has static methods to print the menu and to
 * iterate over user input to add an item, print the inventory or buy or sell
 * a product from the inventory.
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
public class Assign3 {

  /**
   * Start of the main method. Prints the menu and asks for user input.
   * Initiates the menu option and exits if exit is entered.
   * 
   * @param args arguments of the command line
   */
  public static void main(String[] args) {
    boolean isQuit = false;
    boolean inputFlag = false;
    boolean updateFlag = false;
    LocalDate today = LocalDate.now();

    Scanner scanner = new Scanner(System.in);
    int input = 0;

    Inventory inventory = new Inventory();

    while (!isQuit) {
      do {
        scanner = new Scanner(System.in);
        displayMenu();
        try {
          input = Math.abs(Integer.parseInt(scanner.nextLine().trim()));
          if (input < 1 || input > 9) {
            throw new IllegalArgumentException("Invalid entry");
          }
          inputFlag = true;
        } catch (NumberFormatException e) {
          System.err.println("Invalid entry\n");
          continue;
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage() + "\n");
          continue;
        } catch (NoSuchElementException e) {
          System.err.println("EOF");
          scanner.close();
          return;
        }
      } while (!inputFlag);
      switch (input) {
        case 1:
          try {
            inventory.addItem(scanner);
          } catch (NoSuchElementException e) {
            System.err.println("EOF");
            return;
          }
          break;
        case 2:
          System.out.println(inventory);
          break;
        case 3:
          try {
            updateFlag = inventory.updateQuantity(scanner, true);          
          } catch (NoSuchElementException e) {
            System.err.println("EOF");
            return;
          }
          if (!updateFlag) {
            System.err.println("Error...could not buy item");
          }
          System.out.println();
          break;
        case 4:
          try {
            updateFlag = inventory.updateQuantity(scanner, false);
          } catch (NoSuchElementException e) {
            System.err.println("EOF");
            return;
          }
          if (!updateFlag) {
            System.err.println("Error...could not sell item");
          }
          System.out.println();
          break;
        case 5:
          inventory.searchForItem(scanner);
          break;
        case 6:
          inventory.removeExpiredItems(today);
          System.out.println();
          break;
        case 7:
          inventory.printExpirySummary();
          System.out.println();
          break;
        case 8:
          boolean tmpFlag = false;
          do {
            try {
              System.out.print("Enter the expiry date of the item (yyyy-mm-dd or none): ");
              String dateString = scanner.nextLine().trim();
              if (!dateString.equalsIgnoreCase("none")) {
                today = LocalDate.parse(dateString);
              } else {
                today = LocalDate.MAX;
              }
              tmpFlag = true;
            } catch (DateTimeParseException e) {
              System.err.println("Could not create date from input, please use format yyyy-mm-dd\n");
            } catch (IndexOutOfBoundsException e) {
              System.err.println(e.getMessage());
            }
          } while (!tmpFlag);
          System.out.println();
          break;
        case 9:
          isQuit = true;
          System.out.println("Exiting...");
          break;
        default:
          break;
      }
      input = 0;
    }
  }

  /**
   * Static method to print the menu.
   */
  public static void displayMenu() {
    System.out.print(
        (new StringBuffer())
        .append("Please select one of the following:\n")
        .append("1: Add Item to Inventory\n")
        .append("2: Display Current Inventory\n")
        .append("3: Buy Item(s)\n")
        .append("4: Sell Item(s)\n")
        .append("5: Search for Item\n")
        .append("6: Remove Expired Items\n")
        .append("7: Print Expiry\n")
        .append("8: Change Today's Date\n")
        .append("9: To Exit\n")
        .append("> ")
    );
  }
}
