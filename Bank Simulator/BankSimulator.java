import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the stage, builds the scene from the FXML file and runs the application.
 * 
 * <br><ul><li>ID: 040918352</li>
 * <li>Course: CST8132 305</li>
 * <li>Assignment: 9</li>
 * <li>Professor: Md.Istiaque Shariar</li>
 * <li>Date: April 19, 2019</li></ul>
 *
 * @author Damir Omelic
 * @since 11.0.2
 * @version 1.0
 */
public class BankSimulator extends Application {

  /**
   * Takes the stage and creates the scene.
   * 
   * @param stage the stage
   * @throws Exception any exception
   */
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("BankSimulator.fxml"));

    Scene scene = new Scene(root); // attach scene graph to scene
    stage.setTitle("Bank Simulator"); // displayed in window's title bar
    stage.setScene(scene); // attach scene to stage
    stage.show(); // display the stage
  }

  /**
   * Start of main method.
   *
   * @param args arguments of the command line
   */
  public static void main(String[] args) {
    // create a BankSimulator object and call its start method
    launch(args);
  }
}