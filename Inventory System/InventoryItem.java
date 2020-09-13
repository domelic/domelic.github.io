import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

/**
 * The {@code InventoryItem} class contains methods to create a directory of food item
 * with their expiry dates, prints out the expiration summaries, and remove expired items.
 * Update the quantity of the items in relation to the expiration date.
 */
public class InventoryItem extends FoodItem implements Comparable<Object> {

  private int itemQuantityInStock;
  private FoodItem item;
  private Queue<LocalDate> expiries;

  /**
   * Default constructor for the {@code InventoryItem} class.
   */
  public InventoryItem() {
    this.itemQuantityInStock = 0;
    this.item = new FoodItem();
    expiries = new LinkedList<LocalDate>();
  }

  /**
   * Constructor for the {@code InventoryItem} class to take in parameters.
   * 
   * @param itemQuantityInStock the item quantity
   * @param item the food item
   * @param expiry the expiration date
   */
  public InventoryItem(int itemQuantityInStock, FoodItem item, LocalDate expiry) {
    this.itemQuantityInStock = itemQuantityInStock;
    this.item = item;
    expiries = new LinkedList<LocalDate>();
    for (int i = 0; i < itemQuantityInStock; i++) {
      this.expiries.add(expiry);
    }
  }

  /**
   * Returns the result of adding an item.
   * 
   * @param scanner input from the keyboard
   * @return true if item is added, false if not
   */
  public boolean addItem(Scanner scanner) {
    return item.addItem(scanner);
  }

  /**
   * Returns the item code.
   * 
   * @return int item code
   */
  public int getItemCode() {
    return item.getItemCode();
  }

  /**
   * Returns the result of inputting an item code.
   * 
   * @param scanner input from the keyboard
   * @return true if added, false if not
   */
  public boolean inputCode(Scanner scanner) {
    return item.inputCode(scanner);
  }

  /**
   * Prints the expiration date summary for the item.
   */
  public void printExpirySummary() {
    System.out.println("Expiry Details:");
    Object dates[] = expiries.toArray();
    Arrays.sort(dates);
    LocalDate uniqueDates[] = Arrays.stream(dates).distinct().toArray(LocalDate[]::new);
    int count[] = new int[uniqueDates.length];
    for (int i = 0; i < uniqueDates.length; i++) {
      for (int j = 0; j < dates.length; j++) {
        if (uniqueDates[i].compareTo((LocalDate) dates[j]) == 0) {
          ++count[i];
        }
      }
    }
    for (int i = uniqueDates.length - 1 ; i >= 0; i--) {
      System.out.println(uniqueDates[i] + ": " + count[i]);
    }
  }

  /**
   * Removes items that have expired.
   * 
   * @param LocalDate today's date
   */
  public void removeExpiredItems(LocalDate today) {
    Object dates[] = this.expiries.toArray();
    Arrays.sort(dates);
    int count = 0;
    Queue<LocalDate> temporary = new LinkedList<LocalDate>();
    for (int i = 0; i < dates.length; i++) {
      if (!((LocalDate) dates[i]).isBefore(today)) {
        temporary.offer((LocalDate) dates[i]);
        ++count;
      }
    }
    itemQuantityInStock = count;
    this.expiries = temporary;
  }

  /**
   * Compares the item codes of the food items and determines if one comes
   * before the other, and returns the appropriate integer value, 0 if equal,
   * 1 if greater than, -1 if less than.
   * 
   * @param temp the object that we're comparing to
   * @return 0 if equal, 1 if greater than, -1 if less than
   */
  @Override
  public int compareTo(Object temporary) {
    InventoryItem temp = (InventoryItem) temporary;

    if (item.getItemCode() > temp.getItemCode()) {
      return 1;
    } else if (item.getItemCode() < temp.getItemCode()) {
      return -1;
    }
    return 0;
  }

  /**
   * Adds or removes a quantity of the item.
   * 
   * @param scanner input from the keyboard
   * @param amount the quantity to add or remove
   * @return true if removed, false if not
   */
  public boolean updateQuantity(Scanner scanner, int amount) {
    if (itemQuantityInStock + amount < 0) {
      System.err.println("Insufficient stock in inventory...");
      return false;
    }
    
    String dateString = "";
    LocalDate date = LocalDate.now();
    boolean tmpFlag = false;
    do {
      try {
        System.out.print("Enter the expiry date of the item (yyyy-mm-dd or none): ");
        dateString = scanner.nextLine().trim();
        date = LocalDate.now();
        if (!dateString.equalsIgnoreCase("none")) {
          date = LocalDate.parse(dateString);
        } else {
          date = LocalDate.MAX;
        }
        if (amount > 0) {
          Queue<LocalDate> temporary = expiries;
          for (int i = 0; i < amount; i++) {
            temporary.offer(date);
          }
          Object dates[] = temporary.toArray();
          Arrays.sort(dates);
          temporary.clear();
          for (int i = 0; i < dates.length; i++) {
            temporary.offer((LocalDate) dates[i]);
          }
          expiries = temporary;
        } else {
          Queue<LocalDate> temporary = new LinkedList<LocalDate>();
          Object dates[] = expiries.toArray();
          Arrays.sort(dates);
          int count = 0;
          for (int i = 0; i < dates.length; i++) {
            if (((LocalDate) dates[i]).compareTo(date) != 0) {
              temporary.offer((LocalDate) dates[i]);
              ++count;
            }
          }
          while (count++ < itemQuantityInStock - Math.abs(amount)) {
            temporary.offer(date);
          }
          expiries = temporary;
          }
        tmpFlag = true;
      } catch (DateTimeParseException e) {
        System.err.println("Could not create date from input, please use format yyyy-mm-dd\n");
      } catch (IndexOutOfBoundsException e) {
        System.err.println(e.getMessage());
      }
    } while (!tmpFlag);

    itemQuantityInStock += amount;

    return true;
  }

  /**
   * Overrides the string implementation.
   * 
   * @return String the string implementation
   */
  @Override
  public String toString() {
    return item.toString() + " qty: " + itemQuantityInStock + "\n";
  }
}