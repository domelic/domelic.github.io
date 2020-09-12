 *
 * <p>Each test run executes for a different parameter, and on each
 * initialization, the return conditions are checked against the 
 * control statements.
 *
 * <p>This class first generates a random string using Math.random() methods,
 * and later feeds this string to the <code>SortedStringArray</code> class
 * methods as part of the test. 
 *
 * <ul><li>Section: 303</li>
 * <li>Lab teacher: Mohammad Patoary</li></ul>
 *
 * @author Damir Omelic
 * @version 1.0
 * @since 2018-07-30
   */
public class Assign4 {

  /**
   * Start of the <tt>main</tt> method.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    // Generate a new random unsorted string array

    // lower bound biased distribution from 3 to 20
    String[] randStr =
     new String[(int) Math.round(Math.pow(Math.random() + 0.4625, 2) * 8.45) + 1];
    String[] tempStr = new String[randStr.length];

    for (int i = 0; i < randStr.length; i++) {
      randStr[i] = "";
      tempStr[i] = "";
    }

    for (int i = 0; i < randStr.length; i++) {
      // descending distribution from 3 to 20
      for (int j = 0; j < (int) Math.round(Math.pow(Math.random() + 0.5625, 2) * 8.45); j++) {
        // uniform distribution with biases from A to Z
        randStr[i] +=
          (char) ((Math.round(Math.pow(Math.random() + 0.7025, 2) * 10.50625) - 5) + 65);
      }
    }

    // randomize lower case
    for (int i = 0; i < randStr.length; i++) {
      for (int j = 0; j < randStr[i].length(); j++) {
        if ((int) Math.round(Math.random()) == 1) {
          tempStr[i] += Character.toLowerCase(randStr[i].charAt(j));
        } else {
          tempStr[i] += randStr[i].charAt(j);
        }
      }
    }

    randStr = tempStr;

    // Test that the default constructor works properly

    SortedStringArray ssa = new SortedStringArray();
    boolean testSucceeds = true;
    int testRuns = 50;
    String[] errorMessage = {"", ""};

    for (int i = 0; i <= testRuns; i++) {
      if (ssa.size() != 0) {
        errorMessage[0] = "Default constructor: initial size is wrong";
        testSucceeds = false;
      }
      if (ssa.capacity() != 10) {
        errorMessage[1] = "Default constructor: initial capacity is wrong";
        testSucceeds = false;
      }
      if (!testSucceeds) {
        i = testRuns + 1;
      }
    }

    if (testSucceeds) {
      System.out.println("Default constructor: working correctly");
    } else {
      for (int i = 0; i < errorMessage.length; i++) {
        System.err.println(errorMessage[i]);
      }
    }

    // Test that the initial constructor works properly

    testSucceeds = true;
    for (int i = 0; i < errorMessage.length; i++) {
      errorMessage[i] = "";
    }

    for (int i = 0; i <= testRuns; i++) {
      ssa = new SortedStringArray(i);
      if (ssa.size() != 0) {
        errorMessage[0] = "Initial constructor: initial size is wrong";
        testSucceeds = false;
      }
      if (ssa.capacity() != i) {
        errorMessage[1] = "Initial constructor: initial capacity is wrong";
        testSucceeds = false;
      }
      if (!testSucceeds) {
        i = testRuns + 1;
      }
    }

    if (testSucceeds) {
      System.out.println("Initial constructor: working correctly");
    } else {
      for (int i = 0; i < errorMessage.length; i++) {
        System.err.println(errorMessage[i]);
      }
    }

    // Test that the add method works properly

    ssa = new SortedStringArray();

    testSucceeds = true;
    for (int i = 0; i < errorMessage.length; i++) {
      errorMessage[i] = "";
    }

    // General test for condition checking
    for (int i = 0; i < randStr.length; i++) {
      int oldSize = ssa.size();
      boolean testAdd = ssa.add(randStr[i]);
      if (testAdd && ssa.size() - 1 == oldSize) {
        continue;
      } else {
        errorMessage[0] = "Add: capacity is increased but size is not increased";
      }
      if (!testAdd && ssa.size() == oldSize) {
        continue;
      } else {
        errorMessage[1] = "Add: capacity is fixed but size is not fixed";
      }
      testSucceeds = false;
      i = randStr.length;
    }

    SortedStringArray addTest = new SortedStringArray();
    String[] testArray = {"flail", "seize", "birth", "cuneo", "belga"};
    for (int i = 0; i < testArray.length; i++) {
      addTest.add(testArray[i]);
    }

    // Specific test for correct sorting
    for (int i = 0; i < testArray.length; i++) {
      switch (addTest.get(i)) {
        case "belga":
          if (i != 0) {
            testSucceeds = false;
          }
          break;
        case "birth":
          if (i != 1) {
            testSucceeds = false;
          }
          break;
        case "cuneo":
          if (i != 2) {
            testSucceeds = false;
          }
          break;
        case "flail":
          if (i != 3) {
            testSucceeds = false;
          }
          break;
        case "seize":
          if (i != 4) {
            testSucceeds = false;
          }
          break;
        default:
          break;
      }
    }

    if (testSucceeds) {
      System.out.println("Add: working correctly");
    } else {
      System.err.println("Add: not working correctly");
      for (int i = 0; i < errorMessage.length; i++) {
        if (errorMessage[i] != "") {
          System.err.println(errorMessage[i]);
        }
      }
    }

    // Test that the get method works properly
    
    testSucceeds = true;

    // General test for condition checking
    for (int i = 0; i < testRuns; i++) {
      String getStr = ssa.get(i);
      if (i <= ssa.capacity()) {
        if (getStr == null && i >= ssa.size()) {
          continue;
        }
        for (String test : randStr) {
          if (getStr == test) {
            continue;
          }
        }
      } else if (getStr == "ERROR") {
        continue;
      } else {
        testSucceeds = false;
        i = testRuns;
      }
    }

    // Specific test for return value
    for (int i = 0; i < testArray.length; i++) {
      switch (addTest.get(i)) {
        case "belga":
          if (i != 0) {
            testSucceeds = false;
          }
          break;
        case "birth":
          if (i != 1) {
            testSucceeds = false;
          }
          break;
        case "cuneo":
          if (i != 2) {
            testSucceeds = false;
          }
          break;
        case "flail":
          if (i != 3) {
            testSucceeds = false;
          }
          break;
        case "seize":
          if (i != 4) {
            testSucceeds = false;
          }
          break;
        default:
          break;
      }
    }

    if (testSucceeds) {
      System.out.println("Get: working correctly");
    } else {
      System.err.println("Get: not working correctly");
    }

    // Test that the getIndex method works properly

    boolean[] getFlag = {true, true};
    String[] testerStr =
    {"EBAKwREq", "VxcinWGXLB", "bPZ", "cBUb", "gGwcFwO",
      "gUDXQBnez", "hqjfeblI", "iIWNqxqOB", "uiEzJz", "zokcv"};
    String[][] testPass = {randStr, testerStr}; // number of passes
    testSucceeds = true;

    // General test for condition checking
    // First pass checks for redundancies against the randomly generated string
    // Second pass checks for retrieval against an arbitrary string
    for (int k = 0; k < testPass.length; k++) {
      for (int i = 0; i < ssa.size(); i++) {
        for (int j = 0; j < testPass[k].length; j++) {
          if (randStr[ssa.getIndex(randStr[i])] != testPass[k][j]) {
            getFlag[k] = false; // guaranteed to pass on the redundant test
          }
          if (ssa.getIndex(testPass[k][j]) != -1) {
            getFlag[k] = false; // guaranteed to pass against arbitrary string
          }
        }
      }
    }

    if (getFlag[0] && getFlag[1]) {
      testSucceeds = false;
    }

    // Specific test for return value
    for (int i = 0; i < testArray.length; i++) {
      switch (addTest.getIndex(testArray[i])) {
        case 0:
          if (i != 4) {
            testSucceeds = false;
          }
          break;
        case 1:
          if (i != 2) {
            testSucceeds = false;
          }
          break;
        case 2:
          if (i != 3) {
            testSucceeds = false;
          }
          break;
        case 3:
          if (i != 0) {
            testSucceeds = false;
          }
          break;
        case 4:
           if (i != 1) {
            testSucceeds = false;
           }
           break;
        default:
          break;
      }
    }

    if (testSucceeds) {
      System.out.println("GetIndex: working correctly");
    } else {
      System.err.println("GetIndex: not working correctly");
    }

    // Test that the delete method works properly

    testSucceeds = true;

    // General tests for condition checking
    SortedStringArray[] runs = new SortedStringArray[2];
    for (int k = 0; k < runs.length; k++) {
      runs[k] = new SortedStringArray();
      for (int i = 0; i < ssa.capacity(); i++) {
        if (ssa.get(i) != null) {
          runs[k].add(ssa.get(i));
        }
      }
    }

    for (int k = 0; k < runs.length; k++) {
      if (k == 0) { // bottom-up deletes first till end
        for (int i = 0; i < testRuns; i++) {
          int size = runs[k].size();
          if (runs[k].delete(k)) {
            if (runs[k].size() != 1) {
              if (size == runs[k].size() + 1) {
                continue;
              }
            } else {
              if (size == runs[k].size()) {
                continue;
              }
            }
          } else {
            testSucceeds = false;
            i = testRuns;
          }
        }
      } else if (testSucceeds) { //top-down deletes last till end
        for (int i = testRuns; i >= 0; i--) {
          int size = runs[k].size();
          if (i < runs[k].capacity() && runs[k].delete(i)) {
            if (size == runs[k].size() + 1) {
              continue;
            }
          } else if (!runs[k].delete(i)) {
            if (size == runs[k].size()) {
              continue;
            }
          } else {
            testSucceeds = false;
            i = -1;
          }
        }
      }
    }

    SortedStringArray runsTwo = new SortedStringArray();
    for (int i = 0; i < randStr.length; i++) {
      runsTwo.add(randStr[i]);
    }

    for (int i = runsTwo.size() - 1; i >= 0; i--) {
      int size = runsTwo.size();
      if (runsTwo.delete(randStr[i])) {
        if (runsTwo.size() != 1) {
          if (size == runsTwo.size() + 1) {
            continue;
          }
        } else {
          if (size == runsTwo.size()) {
            continue;
          }
        }
      } else {
        testSucceeds = false;
        i = testRuns;
      }
    }

    // Specific tests for delete checking
    String[] testingStr = {"belga", "birth", "cuneo", "flail", "seize"};

    addTest.delete(3);

    for (int i = 0, j = 0; i < testingStr.length; i++) {
      if (!testingStr[j].equals("flail")) {
        if (!addTest.get(i).equals(testingStr[j++])) {
          testSucceeds = false;
          i = testingStr.length;
        }
      }
    }

    addTest.delete("belga");
    
    for (int i = 0, j = 0; i < testingStr.length; i++) {
      if (!testingStr[j].equals("flail") && !testingStr[j].equals("belga")) {
        if (!addTest.get(i).equals(testingStr[j++])) {
          testSucceeds = false;
          i = testingStr.length;
        }
      }
    }

    if (testSucceeds) {
      System.out.println("Delete: working correctly");
    } else {
      System.err.println("Delete: not working correctly");
    }
  }
}
