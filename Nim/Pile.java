/**
 * <h1>Pile</h1>
 *
 * <p>The <code>Pile</code> class contains the <tt>size</tt> of the pile, and
 * methods to retrieve the pile size and remove an amount from the pile size.
 *
 * <ul><li>Section: 303</li>
 * <li>Lab teacher: Mohammad Patoary</li></ul>
 *
 * @author Damir Omelic
 * @version 1.0
 * @since 2018-07-13
 */
public class Pile {

  private int size;

  /**
   * Constructs a new <code>Pile</code> and initializes the size to 10.
   */
  public Pile() {
    this.size = 10;
  }

  /**
   * Constructs a new <code>Pile</code> and initializes the size to the
   * parameter.
   *
   * @param size integer value
   */
  public Pile(int size) {
    this.size = size;
  }

  /**
   * Gets the integer value of the <tt>size</tt> of the pile.
   *
   * @return the pile size
   */
  public int getSize() {
    return this.size;
  }

  /**
   * Removes the <tt>amount</tt> from the pile <tt>size</tt>.
   *
   * @param amount amount to remove from the pile
   */
  public void remove(int amount) {
    this.size -= amount;
  }
}