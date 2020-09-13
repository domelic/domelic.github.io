import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Collections;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * The {@code: Inventory} class has static methods to print the menu and to
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
public class Inventory {

  /**
   * Declare the {@code: FoodItem} array.
   */
  private ArrayList<InventoryItem> inventory;

  /**
   * Declare the capacity of the array.
   */
  private int numItems;

  /**
   * Constructor of the {@code: Inventory} class. Initializes array and capacity.
   */
  public Inventory() {
    inventory = new ArrayList<InventoryItem>(Collections.nCopies(20, new InventoryItem()));
    numItems = 0;
  }

  /**
   * String to return the item details in the inventory array.
   *
   * @return string with the details of the items in the inventory
   */
  @Override
  public String toString() {
    String str = "Inventory:\n";
    for (int i = 0; i < inventory.size(); i++) {
      if (inventory.get(i).getItemCode() != 0) {
        str += inventory.get(i).toString();
      }
    }
    return str;
  }

  /**
   * Searches for an item using binary search.
   * 
   * @param scanner input from the keyboard
   * @throws NoSuchElementException if EOF is entered
   */
  public void searchForItem(Scanner scanner) throws NoSuchElementException {
    for (int i =  0; i < inventory.get(i).getItemCode(); i++) {
      System.out.println(inventory.get(i).getItemCode());
    }
    int code = 0;
    boolean notValid = false;
    int index = 0;

    do {
      notValid = false;
      if (numItems == 0) {
        System.err.println("Inventory is empty.\n");
        return;
      }
      System.out.print("Enter the code for the item: ");
      try {
        code = Integer.parseInt(scanner.nextLine().trim());
        if (code < 0) {
          throw new IllegalArgumentException("Invalid entry");
        }
      } catch (NumberFormatException e) {
        notValid = true;
        System.err.println("Invalid entry");
      } catch (IllegalArgumentException e) {
        notValid = true;
        System.err.println(e.getMessage());
      }
    } while (notValid);

    ArrayList<Integer> codes = new ArrayList<Integer>();
    for (InventoryItem i : inventory) {
      codes.add(i.getItemCode());
    }
    if ((index = Collections.binarySearch(codes, code)) < 0) {
      System.out.println("Code not found in inventory...\n");
    } else {
      System.out.println(inventory.get(index).toString());
    }
  }

  public void printExpirySummary() {
    Scanner scanner = new Scanner(System.in);
    int code = 0;
    boolean notValid = false;
    int searchKey = -1;

    do {
      notValid = false;
      if (numItems == 0) {
        System.err.println("Inventory is empty.\n");
        return;
      }
      System.out.print("Enter the code for the item: ");
      try {
        code = Integer.parseInt(scanner.nextLine().trim());
        if (code < 0) {
          throw new IllegalArgumentException("Invalid entry");
        }
      } catch (NumberFormatException e) {
        notValid = true;
        System.err.println("Invalid entry");
      } catch (IllegalArgumentException e) {
        notValid = true;
        System.err.println(e.getMessage());
      }
    } while (notValid);
    ArrayList<Integer> codes = new ArrayList<Integer>();
    for (InventoryItem singleItem : inventory) {
      codes.add(singleItem.getItemCode());
    }
    if ((searchKey = Collections.binarySearch(codes, code)) < 0) {
      System.out.println("Code not found in inventory...\n");
    } else {
      System.out.print(inventory.get(searchKey).toString());
      inventory.get(searchKey).printExpirySummary();
    }
  }

  public void removeExpiredItems(LocalDate today) {
    for (int i = 0; i < inventory.size(); i++) {
      inventory.get(i).removeExpiredItems(today);
    }
  }

  /**
   * Checks if an item parameter has a code that matches this item. Returns the
   * index of the item, otherwise returns -1.
   * 
   * @param item the {@code: FoodItem} object parameter
   * @return the index of the item with the matching code, -1 if not found
   */
  public int alreadyExists(InventoryItem item) {
    for (int i = 0; i < numItems; i++) {
      if (inventory.get(i).getItemCode() == item.getItemCode()) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Method to add an item to the array. Asks first for type, then initializes
   * the inventory item to the the according item type. Asks for code and checks 
   * if unique, then reference to add item method of the {@code: FoodItem} class.
   * 
   * @param scanner input from the keyboard
   * @return true if item is added, otherwise false
   * @throws NoSuchElementException if reaching EOF
   */
  public boolean addItem(Scanner scanner) throws NoSuchElementException {
    boolean choiceFlag = false;
    char choice = ' ';
    int itemQuantity = 0;
    LocalDate date = LocalDate.now();
    FoodItem item = new FoodItem();

    do {
      System.out.print(
          "Do you wish to add a fruit(f), vegetable(v) or a preserve(p)? "
      );
      try {
        choice = scanner.nextLine().trim().toLowerCase().charAt(0);
        if (choice != 'f' && choice != 'v' && choice != 'p') {
          throw new IllegalArgumentException("Invalid entry");
        }
        choiceFlag = true;
        System.out.println();
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\n");
      } catch (StringIndexOutOfBoundsException e) {
        System.err.println("Invalid entry\n");
      }
    } while (!choiceFlag);
    try {
      if (choice == 'f') {
        item = new Fruit();
      }
      if (choice == 'v') {
        item = new Vegetable();
      }
      if (choice == 'p') {
        item = new Preserve();
      }
      boolean tmpFlag = true;
      do {
        tmpFlag = true;
        item.inputCode(scanner);
        for (int i = 0; i < inventory.size(); i++) {
          if (item.getItemCode() == inventory.get(i).getItemCode()) {
            System.err.println("Item code already exists");
            // inventory.get(numItems).inputCode(scanner);
            tmpFlag = false;
          }
        }
      } while (!tmpFlag);
      item.addItem(scanner);
      do {
        System.out.print("Enter the quantity for the item: ");
        try {
          itemQuantity = Integer.parseInt(scanner.nextLine().trim());
          if (itemQuantity < 0) {
            throw new IllegalArgumentException("Invalid entry");
          }
          tmpFlag = true;
        } catch (NumberFormatException e) {
          System.err.println("Invalid entry\n");
          tmpFlag = false;
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage() + "\n");
          tmpFlag = false;
        }
      } while (!tmpFlag);
      tmpFlag = false;
      do {
        try {
          System.out.print("Enter the expiry date of the item (yyyy-mm-dd or none): ");
          String dateString = scanner.nextLine().trim();
          if (!dateString.equalsIgnoreCase("none")) {
            date = LocalDate.parse(dateString);
          } else {
            date = LocalDate.MAX;
          }
          tmpFlag = true;
        } catch (DateTimeParseException e) {
          System.err.println("Could not create date from input, please use format yyyy-mm-dd\n");
        }
      } while (!tmpFlag);
      if (choice == 'f') {
        inventory.set(numItems, new InventoryItem(itemQuantity, item, date));
      }
      if (choice == 'v') {
        inventory.set(numItems, new InventoryItem(itemQuantity, item, date));
      }
      if (choice == 'p') {
        inventory.set(numItems, new InventoryItem(itemQuantity, item, date));
      }
      ++numItems;
      Collections.sort(inventory);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.err.println("Inventory is full.");
      return false;
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage() + "\n");
      return false;
    }
    System.out.println();
    return true;
  }

  /**
   * Method to ask for code to search for in the array. If not found, exits.
   * If found, asks for quantity to buy (or sell) and updates it. Checks for
   * each parameter if valid and within the user defined bounds.
   * 
   * @param scanner input from the keyboard
   * @param buyOrSell true if buying, false if selling
   * @return true if quantity is updated, false otherwise
   * @throws NoSuchElementException if reaching EOF
   */
  public boolean updateQuantity(Scanner scanner, boolean buyOrSell) throws NoSuchElementException {
    boolean inputFlag = false;
    int code = 0;
    int amount = 0;
    int searchKey = -1;

    if (numItems == 0) {
      return false;
    }
    do {
      System.out.print("Enter valid item code: ");
      try {
        code = Integer.parseInt(scanner.nextLine().trim());
        if (code < 0) {
          throw new IllegalArgumentException("Invalid code");
        }
        inputFlag = true;
      } catch (NumberFormatException e) {
        System.err.println("Invalid code");
        return false;
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\n");
        return false;
      }
    } while (!inputFlag);

    ArrayList<Integer> codes = new ArrayList<Integer>();
    for (InventoryItem i : inventory) {
      codes.add(i.getItemCode());
    }
    if ((searchKey = Collections.binarySearch(codes, code)) < 0) {
      System.out.println("Code not found in inventory...\n");
      return false;
    }

    // if (!buyOrSell && inventory.get(searchKey).itemQuantityInStock == 0) {
    //   inventory.get(searchKey).updateQuantity(scanner, -1);
    //   return false;
    // }
    
    inputFlag = false;
    do {
      if (buyOrSell) {
        System.out.print("Enter valid quantity to buy: ");
      } else {
        System.out.print("Enter valid quantity to sell: ");
      }
      try {
        amount = Integer.parseInt(scanner.nextLine().trim());
        if (amount < 0) {
          throw new IllegalArgumentException("Invalid quantity...");
        }
        inputFlag = true;
      } catch (NumberFormatException e) {
        System.err.println("Invalid quantity...");
        return false;
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\n");
        return false;
      }
    } while (!inputFlag);

    if (!buyOrSell) {
      amount *= (-1);
    }

    if (!inventory.get(searchKey).updateQuantity(scanner, amount)) {
      return false;
    }
    return true;
  }
}