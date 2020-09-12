/**
 * <h1>Assign3</h1>
 *
 * <p>The <code>Assign3</code> program starts the nim game and displays the
 * game header. If <tt>playerFirst</tt> is set to <code>true</code>, the player
 * takes the first move. Otherwise, if it's set to <code>false</code>, the
 * computer takes the first move. The game continues until there are no more
 * moves left.
 *
 * <ul><li>Section: 303</li>
 * <li>Lab teacher: Mohammad Patoary</li></ul>
 *
 * @author Damir Omelic
 * @version 1.0
 * @since 2018-07-13
 */
public class Assign3 {

  /**
   * Start of the <tt>main</tt> method.
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Nim nim = new Nim();

    System.out.printf("Welcome to the NIM game\n"
        + "We play by the misère rules\n");
    nim.printPiles();
    System.out.println();

    boolean playerFirst = true;

    if (playerFirst) {
      nim.playerMove();
    } else {
      nim.computerMove();
    }
  }
}
