/**
 * <h1>SortedStringArray</h1>
 *
 * <p>The <code>SortedStringArray</code> class contains methods to take a
 * string parameter and lexicographically sort it using insertion sort into
 * an array of strings.
 *
 * <p>If a flag is set to true, the array will increase in size if adding
 * strings over its initial capacity.
 *
 * <p>The class also contains methods to retrieve the index of a string in the
 * array, the string at an index in the array, and to display the entire string
 * array.
 *
 * <p>The class also contains a method to remove an entry at the given index,
 * and if a flag is set to true, also scale down the array capacity.
 *
 * <ul><li>Section: 303</li>
 * <li>Lab teacher: Mohammad Patoary</li></ul>
 *
 * @author Damir Omelic
 * @version 1.0
 * @since 2018-07-30
 */
public class SortedStringArray {

  private String[] stringArray;
  private int currentSize;

  /**
   * Constructs the <code>SortedStringArray</code> class and initializes the
   * capacity of the string array and the number of string elements in it to
   * zero.
   */
  public SortedStringArray() {
    stringArray = new String[10];
    currentSize = 0;
  }

  /**
   * Constructs the <code>SortedStringArray</code> class and initializes the
   * capacity of the string array to <tt>initialCapacity</tt> and the number of
   * string elements in it to zero.
   *
   * @param initialCapacity initial array capacity
   */
  public SortedStringArray(int initialCapacity) {
    stringArray = new String[initialCapacity];
    currentSize = 0;
  }

  /**
   * Takes a string and adds it to the array. If the size of the array is equal
   * to its capacity, the capacity will increase by one and then the string will
   * added. If not, the method will return false.
   *
   * <p>Once a string is added, it's lexicographyically sorted at the correct
   * index position in the array using insertion sort.
   *
   * @param s string that's added
   * @return true if string is added, otherwise false
   */
  public boolean add(String s) {
    boolean increaseCapacity = true;

    if (++currentSize > stringArray.length && increaseCapacity) {
      String[] tempArray = new String[stringArray.length + 1];
      System.arraycopy(stringArray, 0, tempArray, 0, stringArray.length);
      stringArray = tempArray;
    }

    try {
      stringArray[currentSize - 1] = s;
      for (int i = 1; i < currentSize; i++) {
        int j;
        String strCopy = stringArray[i];
        for (j = i - 1; j >= 0 && stringArray[j].compareToIgnoreCase(strCopy) > 0; j--) {
          stringArray[j + 1] = stringArray[j];
        }
        stringArray[j + 1] = strCopy;
      } 
    } catch (ArrayIndexOutOfBoundsException e) {
      --currentSize;
      return false;
    }
    return true;
  }

  /**
   * Shows the current state of the array. IF a flag is set to true, the method
   * will print out all the elements in the array, <tt>null</tt>'s included.
   * Otherwise, the method will print only the string elements in the array.
   *
   * @throws ArrayIndexOutOfBoundsException if accessing illegal index
   */
  public void print() {
    boolean printAll = false;

    if (currentSize != 0) {
      System.out.print('[');
    }
    int printLength = (printAll) ? stringArray.length : currentSize;
    try {
      for (int i = 0; i < printLength; i++) {
        System.out.print(stringArray[i]);
        if (i != printLength - 1) {
          System.out.print(", ");        
        } else {
          System.out.println(']');
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      return;
    }
  }

  /**
   * Retrieves the string at the position of <tt>index</tt>.
   *
   * @param index index to return
   * @return String if accessing legal index, otherwise "ERROR" string
   * @throws ArrayIndexOutOfBoundsException if accessing illegal index
   */
  public String get(int index) {
    try {
      return stringArray[index];
    } catch (ArrayIndexOutOfBoundsException e) {
      return "ERROR";
    }
  }

  /**
   * Traverses the array in search for a string and returns the index if the
   * string is found, otherwise returns -1.
   *
   * @param entry String that's searched
   * @return index of the string if found, otherwise -1
   */
  public int getIndex(String entry) {
    for (int index = 0; index < currentSize; index++) {
      if (stringArray[index].equals(entry)) {
        return index;
      }
    }

    return -1;
  }

  /**
   * Removes the string at the index position and shifts the array back into
   * ordered position and returns true. If index is illegal, returns false.
   *
   * @param index position of string to be removed
   * @return true if string is successfully removed, otherwise false
   * @throws ArrayIndexOutOfBoundsException if accessing illegal index
   */
  public boolean delete(int index) {
    boolean decreaseCapacity = false;

    try {
      stringArray[index] = null;
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }

    for (int i = index; i < currentSize - 1; i++) {
      stringArray[i] = stringArray[i + 1];
    }

    if (decreaseCapacity) {
      int tempSize = currentSize;
      if (currentSize == 0) {
        tempSize = 1;
      }
      String[] tempArray = new String[tempSize];
      System.arraycopy(stringArray, 0, tempArray, 0, tempArray.length);
      stringArray = tempArray;
    }

    if (currentSize != 1) {
      --currentSize;      
    } 

    return true;
  }

  /**
   * Removes the entry string and shifts the array back into ordered
   * position and returns true. If string entry not found, returns false;
   *
   * @param entry string to be removed
   * @return true if string is successfully removed, otherwise false
   */
  public boolean delete(String entry) {
    boolean decreaseCapacity = false;

    if (currentSize == 1 && stringArray[0].equals(entry)) {
      stringArray[0] = null;
      return true;
    }

    for (int i = 0; i < currentSize; i++) {
      if (stringArray[i].equals(entry)) {
          if (stringArray[0] != null) {
          for (int j = i; j < stringArray.length - 1; j++) {
            stringArray[j] = stringArray[j + 1];
          }

          if (decreaseCapacity) {
            int tempSize = currentSize;
            if (currentSize == 0) {
              tempSize = 1;
            }
            String[] tempArray = new String[tempSize];
            System.arraycopy(stringArray, 0, tempArray, 0, tempArray.length);
            stringArray = tempArray;
          }

          if (currentSize != 1) {
            --currentSize;
          }
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Getter for the array size.
   *
   * @return array size
   */
  public int size() {
    return currentSize;
  }

  /**
   * Getter for the array capacity.
   *
   * @return array capacity
   */
  public int capacity() {
    return stringArray.length;
  }
}