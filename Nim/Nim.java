import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * <h1>Nim</h1>
 * 
 * <p>The <code>Nim</code> class contains methods to initialize the piles and
 * their starting values, as well as all the rules pertaining to the valid
 * player and computer moves (both for random and non-random moves).
 *
 * <p>At the start and at the end of the player move method, a method to check
 * if all the piles are zero is called, and if they are, the game terminates.
 * If they're not, the game continues until they are.
 *
 * <p>After every player and computer move, a method to print the current state
 * of the piles is called. The state of the piles is not shown if there are no
 * more moves left and the game terminates with the message of who won the
 * game, either the player or the computer.
 *
 * <p>The non-random computer moves take into account the nim sum winning
 * strategy, and the misère move (to revert from the winning strategy) is
 * properly implemented and calculated.
 *
 * <p>The game is configured so it works for any number of starting piles, as
 * long as the new <tt>Pile</tt> instance variables are properly declared.
 *
 * <ul><li>Section: 303</li>
 * <li>Lab teacher: Mohammad Patoary</li></ul>
 *
 * @author Damir Omelic
 * @version 1.0
 * @since 2018-07-13
 */
public class Nim {

  private Pile pileA; // First pile
  private Pile pileB; // Second pile
  private Pile pileC; // Third pile

  private Random rnd = new Random();
  private Scanner input = new Scanner(System.in); 

  /**
   * Constructs the <code>Nim</code> class and initializes the piles to a
   * random value between the <tt>size</tt> constant and the next size after.
   * 
   * <p>Instance variables are reflectively initialized by looping through
   * all of the declared <tt>Pile</tt> objects.
   *
   * <p>The trivial games where two heaps start with the same size are
   * eliminated by checking if the random initialization set these piles
   * to be equal, and if they are, the loop continues until the condition is
   * satisfied.
   *
   * @throws IllegalAccessException if cannot access the fields
   */
  public Nim() {
    boolean gameIsTrivial;
    Field[] fields = Nim.class.getDeclaredFields();
    int[] heaps = new int[fields.length - 2];
    final int size = 10;

    do {
      gameIsTrivial = false;
      try {
        for (int i = 0; i < heaps.length; i++) {
          fields[i].set(this, new Pile(rnd.nextInt(size + 1) + size));
          Field field = fields[i];
          field.setAccessible(true);
          if (!(field.getType().isAssignableFrom(Pile.class))) {
            continue;
          }
          Pile pile = (Pile) field.get(this);
          heaps[i] = pile.getSize();
        }
      } catch (IllegalAccessException e) {
        System.err.println(e);
      }
      int[] heapsCopy = Arrays.copyOf(heaps, heaps.length);
      if (!(heaps.length > size)) {
        for (int i = 0; i < heaps.length; i++) {
          for (int j = heaps.length - 1; j >= 0; j--) {
            if (i != j) {
              if (heapsCopy[j] == heaps[i]) {
                gameIsTrivial = true;
              } // If any two heaps initialize as equal
            }
          }
        }
      }
    } while (gameIsTrivial);
  }

  /**
   * Contains all the rules pertaining to the player moves.
   * 
   * <p>The <code>done()</code> method is called at the start of the game to
   * check if all the piles are zero, and if returning <tt>true</tt>, a message
   * telling the player won will be displayed and the method will be returned.
   *
   * <p>If <code>done()</code> returns <tt>false</tt>, the instance variables
   * are accessed using reflection, and the return values on the <tt>Pile</tt>
   * sizes are stored, as well as the last character of the <tt>Pile</tt>
   * instance variables, and the pile names are then mapped with its
   * <tt>Pile</tt> variables.
   * 
   * <p>Player input is checked to match the name of the pile, and any invalid
   * input asks for the pile selection again. At every input, whitespace is
   * also checked, and as long as the first character(s) of the string match the
   * desired input, the character(s) is caught and the game continues.
   *
   * <p>Whitespace checking takes into account any whitespace entered after the
   * characters are input, and this whitespace is properly discarded.
   *
   * <p>The above is also done when removing a value from the heaps, and the
   * integer value is constructed from the passed string.
   *
   * <p>After the correct input is entered for both the pile and the value,
   * the value will be removed from the selected heap from the <tt>Map</tt>
   * variable.
   *
   * <p>The <code>done()</code> method is called again, and if it returns
   * <tt>true</tt>, the method is returned from with the message telling the
   * player lost.
   *
   * <p>If <code>done()</code> returned false, the <code>computerMove()</code>
   * method will be called if <code>computerIsRandom</code> is set to
   * <code>true</code>, otherwise the <code>computerIsRandom</code> method will
   * be called.
   * 
   * @return boolean return of the method
   * @throws IllegalAccessException if cannot access the fields
   * @throws IllegalArgumentException if entering illegal values
   * @throws ArrayIndexOutOfBoundsException if traversing over the array
   * @throws StringIndexOutOfBoundsException if traversing over the string
   */
  public boolean playerMove() {
    boolean done = done();
    if (done) {
      System.out.println("Congrats: you win");
      return done;
    }

    Field[] fields = Nim.class.getDeclaredFields();
    int[] heaps = new int[fields.length - 2];
    char[] heapLetters = new char[heaps.length];
    Map<Character, Pile> map = new HashMap<Character, Pile>();

    try {
      for (int i = 0; i < heaps.length; i++) {
        Field field = fields[i];
        field.setAccessible(true);
        if (!(field.getType().isAssignableFrom(Pile.class))) {
          continue;
        }
        Pile pile = (Pile) field.get(this);
        heaps[i] = pile.getSize();
        heapLetters[i] = fields[i].getName().replace("pile", "").charAt(0);
        map.put(heapLetters[i], pile);
      }
    } catch (IllegalAccessException e) {
      System.err.println(e);
    }

    boolean errorFlag = false;
    char heapLetter = ' ';
    int value = 0;

    do {
      boolean whitespaceFlag = true;
      int heapSize = 0;
      errorFlag = false;

      try {
        boolean charFlag = false;
        boolean isInteger = true;

        System.out.print("\nSelect a pile: ");

        do {
          String str = input.next();
          for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
              heapLetter = str.charAt(i);
              if (!Character.isWhitespace(heapLetter)) {
                whitespaceFlag = false;
                i = str.length();
              }
            }
          }
        } while (whitespaceFlag);

        input.nextLine();

        heapLetter = Character.toUpperCase(heapLetter);
        for (char letter : heapLetters) {
          if (letter == heapLetter) {
            charFlag = true;
            heapSize = heaps[letter - 'A'];
          }
        }

        if (!charFlag) {
          String errorMessage = "";
          for (int i = 0; i < heapLetters.length; i++) {
            errorMessage += Character.toLowerCase(heapLetters[i]);
            if (i < heapLetters.length - 2) {
              errorMessage += ", ";
            }
            if (i == heapLetters.length - 2) {
              errorMessage += " or ";
            }
          }
          throw new IllegalArgumentException(String.format(
            "Invalid pile letter, select %s", errorMessage));
        }

        if (heapSize == 0) {
          throw new IllegalArgumentException(String.format("Pile %c is empty, "
              + "pick another", Character.toLowerCase(heapLetter)));
        }

        String removeError = String.format("Pick a number between 1 and %d",
            heapSize);

        try {
          int[] amount = new int[heaps.length];
          String initStr = "";
          whitespaceFlag = true;
          value = 0;

          System.out.print("How many do you want to remove? ");

          do {
            initStr = input.nextLine();
            for (int i = 0; i < initStr.length(); i++) {
              if (!Character.isWhitespace(initStr.charAt(i))) {
                whitespaceFlag = false;
                i = initStr.length();
              }
            }
          } while (whitespaceFlag);

          String valueStr = "";

          char[] chars = initStr.toCharArray();
          for (int i = 0; i < chars.length; i++) {
            if (!Character.isWhitespace(chars[i])) {
              valueStr += chars[i];
              try {
                if (Character.isWhitespace(chars[i + 1])) {
                  i = chars.length;
                }
              } catch (ArrayIndexOutOfBoundsException e) {
                if (Character.isWhitespace(chars[chars.length - 1])) {
                  i = chars.length;
                }
              }
            }
          }

          boolean[] isIntegers = new boolean[heaps.length];
          for (int i = 0; i < isIntegers.length; i++) {
            try {
              amount[i] = Integer.parseInt(Character.toString(valueStr.charAt(i)));
              isIntegers[i] = true;
            } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
              isIntegers[i] = false;
            } // If cannot do conversion
          }

          if (isIntegers[0]) {
            value += amount[0];
            for (int i = 1; i < isIntegers.length; i++) {
              if (isIntegers[i]) {
                value *= 10;
                value += amount[i];
              } 
              if (!isIntegers[i]) {
                i = isIntegers.length;
              }
            }
          }

          if (!isIntegers[0] || value < 1 || value > heapSize) {
            throw new IllegalArgumentException(removeError);
          }
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage());
          printPiles();
          System.out.println();
          errorFlag = true;
        }
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage());
        printPiles();
        System.out.println();
        errorFlag = true;
      } 
    } while (errorFlag);

    map.get(heapLetter).remove(value);

    done = done();
    if (done) {
      System.out.println("Sorry: you lose");
      return done;
    }

    printPiles();
    System.out.println();

    boolean computerIsRandom = false;

    if (!computerIsRandom) {
      computerMove();
    } else {
      computerRandomMove();
    }

    return done;
  }

  /**
   * Computer move to take from any <tt>Pile</tt> any amount from 1 to the pile
   * size, as long as the pile is not empty.
   *
   * <p>If the randomly chosen amount is greater than 2, a value of 2 is
   * removed from this amount to prevent trivial games. 
   *
   * <p>Instance variables are accessed using reflection and its sizes and names
   * are stored, as well as a map keying the names with the <tt>Pile</tt>
   * instance variables.
   *
   * <p>If the game is not done, the state of the piles is displayed.
   *
   * <p>At the end of the method, the <code>playerMove()</code> method is 
   * called.
   *
   * @throws IllegalAccessException ig cannot access the fields
   */
  private void computerRandomMove() {
    boolean zeroFlag;
    int value = 0;
    int index = 0;

    Field[] fields = Nim.class.getDeclaredFields();
    int[] heaps = new int[fields.length - 2];
    char[] heapLetters = new char[heaps.length];
    Map<Character, Pile> map = new HashMap<Character, Pile>();

    try {
      for (int i = 0; i < heaps.length; i++) {
        Field field = fields[i];
        field.setAccessible(true);
        if (!(field.getType().isAssignableFrom(Pile.class))) {
          continue;
        }
        Pile pile = (Pile) field.get(this);
        heaps[i] = pile.getSize();
        heapLetters[i] = fields[i].getName().replace("pile", "").charAt(0);
        map.put(heapLetters[i], pile);
      }
    } catch (IllegalAccessException e) {
      System.err.println(e);
    }

    do {
      zeroFlag = false;
      index = rnd.nextInt(heaps.length);
      value = heaps[index];
      if (value == 0) {
        zeroFlag = true;
      } 
    } while (zeroFlag);

    if (value > 2) {
      value -= 2;
    } // If removing entire heap

    map.get(heapLetters[index]).remove(value);

    System.out.printf("Computer takes %d from pile %c\n",
        value, heapLetters[index]);

    if (!done()) {
      printPiles();
      System.out.println();
    }

    playerMove();
  }

  /**
   * Handles all the computer moves using the winning strategy.
   *
   * <p>Instance variables are accessed using reflection and its sizes and names
   * are stored, as well as a map keying the names with the <tt>Pile</tt>
   * instance variables.
   *
   * <p>First, all piles are converted to binary arrays, and then are nim
   * summed (modulo of 2). The nim sum is XOR summed with the heap sizes and
   * the potential values are stored in an array after converting them to
   * decimal.
   *
   * <p>If the nim sum is zero, meaning the next move is a losing move, the
   * computer takes a random amount from the pile, as long as the pile is not
   * zero itself.
   *
   * <p>If nim sum is not zero, the computer uses the winning strategy to
   * remove a value from the pile not greater than the pile itself, as long
   * as the pile is not zero itself.
   * 
   * <p>The misère move is calculated when two heaps are the same size, or
   * the configuration of the heaps is any one of 0, 1, or 2, where accordingly
   * either one more is removed or kept in the heap.
   *
   * <p>If the game is not done, the current state of the piles is displayed.
   *
   * <p>At the end of the method, the <code>playerMove()</code> method is
   * called.
   *
   * @throws IllegalAccessException if cannot access the fields
   */
  public void computerMove() {
    Field[] fields = Nim.class.getDeclaredFields();
    int[] heaps = new int[fields.length - 2];
    char[] heapLetters = new char[heaps.length];
    Map<Character, Pile> map = new HashMap<Character, Pile>();

    try {
      for (int i = 0; i < heaps.length; i++) {
        Field field = fields[i];
        if (!(field.getType().isAssignableFrom(Pile.class))) {
          continue;
        }
        Pile pile = (Pile) field.get(this);
        heaps[i] = pile.getSize();
        heapLetters[i] = fields[i].getName().replace("pile", "").charAt(0);
        map.put(heapLetters[i], pile);
      }
    } catch (IllegalAccessException e) {
      System.err.println(e);
    }

    int[] newHeaps = Arrays.copyOf(heaps, heaps.length);
    int[][] heapsInBinary = new int[heaps.length][5];
    int[] nimSumInBinary = new int[heapsInBinary[0].length];

    for (int i = 0; i < heaps.length; i++) {
      for (int j = heapsInBinary[0].length - 1; j >= 0; j--) {
        heapsInBinary[i][j] = heaps[i] % 2;
        heaps[i] /= 2;
      }
    }

    for (int i = 0; i < nimSumInBinary.length; i++) {
      for (int j = 0; j < heapsInBinary.length; j++) {
        nimSumInBinary[i] += heapsInBinary[j][i];
      }
      nimSumInBinary[i] %= 2;
    }

    int nimSum = 0;
    for (int i = 0; i < nimSumInBinary.length; i++) {
      if (nimSumInBinary[i] == 1) {
        nimSum += Math.pow(2, nimSumInBinary.length - i + 1);
      }
    }

    int[][] xorSumInBinary = new int[heapsInBinary.length][heapsInBinary[0].length];
    for (int i = 0; i < xorSumInBinary.length; i++) {
      for (int j = 0; j < xorSumInBinary[0].length; j++) {
        xorSumInBinary[i][j] = (heapsInBinary[i][j] + nimSumInBinary[j]) % 2;
      }
    }

    int[] xorSum = new int[xorSumInBinary.length];
    for (int i = 0; i < xorSumInBinary.length; i++) {
      for (int j = 0; j < xorSumInBinary[0].length; j++) {
        if (xorSumInBinary[i][j] == 1) {
          xorSum[i] += Math.pow(2, xorSumInBinary[0].length - j - 1);
        }
      }
    }

    boolean sizeFlag = true;
    int index = 0;
    int value = 0;

    if (nimSum == 0) {
      do {
        sizeFlag = true;
        index = rnd.nextInt(newHeaps.length);
        if (newHeaps[index] != 0) {
          value = rnd.nextInt(newHeaps[index]) + 1;
          sizeFlag = false;
          if (value > 2) {
            value -= 2;
          } // If removing entire heap
        }
      } while (sizeFlag);
    } // If cannot play the winning move

    if (nimSum != 0) {
      do {
        sizeFlag = true;
        index = rnd.nextInt(newHeaps.length);
        if (xorSum[index] < newHeaps[index]) {
          value = newHeaps[index] - xorSum[index];
          sizeFlag = false;
        }
      } while (sizeFlag);
    } // If can play the winning move

    int totalInGame = 0;
    for (int i = 0; i < newHeaps.length; i++) {
      totalInGame += newHeaps[i];
    }

    if (totalInGame != 1) {
      for (int i = 0; i < newHeaps.length; i++) {
        if (totalInGame == newHeaps[i]) {
          value = Math.max(newHeaps[i], totalInGame) - 1;
        } // If any two heaps are equal
      }
    }

    int totalLeftInGame = totalInGame - value;
    int remainingInHeap = newHeaps[index] - value;
    if (totalLeftInGame == 2) { // If any heap is 1 and:
      if (remainingInHeap == 0 || remainingInHeap == 2) {
        value--;
      } // if any other heap is 1 or 2
      if (remainingInHeap == 1) {
        value++;
      } // if any other heap is 0
    }
    if (totalLeftInGame == 3 && remainingInHeap == 2) {
      value--;
    } // If heaps are 0, 1 and 2

    map.get(heapLetters[index]).remove(value);

    System.out.printf("Computer takes %d from pile %c\n", value,
        heapLetters[index]);

    if (!done()) {
      printPiles();
      System.out.println();
    }

    playerMove();
  } 

  /**
   * Checks if the game is done by summing up the heap sizes and returning the
   * boolean value of the comparison of the sum with zero.
   *
   * <p>Instance variables are accessed using reflection and its sizes are
   * stored in an array.
   *
   * @return boolean value of the comparison
   * @throws IllegalAccessException if cannot access the fields
   */
  public boolean done() {
    Field[] fields = Nim.class.getDeclaredFields();
    int[] heaps = new int[fields.length - 2];

    try {
      for (int i = 0; i < fields.length; i++) {
        Field field =  fields[i];
        field.setAccessible(true);
        if (!(field.getType().isAssignableFrom(Pile.class))) {
          continue;
        }
        Pile pile = (Pile) field.get(this);
        heaps[i] = pile.getSize();
      }
    } catch (IllegalAccessException e) {
      System.err.println(e);
    }

    int totalInGame = 0;
    for (int i = 0; i < heaps.length; i++) {
      totalInGame += heaps[i];
    }

    return totalInGame == 0;
  }

  /**
   * Prints the current state of the piles.
   *
   * <p>Instance variables are accessed using reflection and its sizes and 
   * names are stored in arrays.
   *
   * @throws IllegalAccessException if cannot access the fields
   */
  public void printPiles() {
    Field[] fields = Nim.class.getDeclaredFields();
    int[] heaps = new int[fields.length - 2];
    Character[] letters = new Character[heaps.length];

    try {
      for (int i = 0; i < heaps.length; i++) {
        Field field = fields[i];
        field.setAccessible(true);
        if (!(field.getType().isAssignableFrom(Pile.class))) {
          continue;
        }
        Pile pile = (Pile) field.get(this);
        heaps[i] = pile.getSize();
        letters[i] = fields[i].getName().replace("pile", "").charAt(0);
      }
    } catch (IllegalAccessException e) {
      System.err.println(e);
    }

    String pile = "";
    for (int i = 0; i < heaps.length; i++) {
      pile += String.format("\t%c", letters[i]);
    }
    pile += "\n";
    for (int i = 0; i < heaps.length; i++) {
      pile += String.format("\t%d", heaps[i]);
    }

    System.out.print(pile);
  }
}