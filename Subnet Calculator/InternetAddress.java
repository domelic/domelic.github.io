package src.calculator;

import java.util.Scanner;

/**
 * The class {@code InternetAddress} contains methods for taking an IPv4 address,
 * the subnet mask and verifying if the address is entered in the correct format
 * and if the mask bits are of an applicable network class.
 *
 * <p>If the address is not entered in CIDR notation, the program prompts for a
 * subnet mask and continues accordingly.
 * 
 * <br><ul><li>ID: 040918352</li>
 * <li>Course: CST8108 012</li>
 * <li>Assignment: Subnet Calculator</li>
 * <li>Professor: Risvan Coskun</li>
 * <li>Date: April 21, 2019</li></ul>
 *
 * @author Damir Omelic
 * @since 11.0.2
 * @version 1.0
 * @see src.calculator
 */
public class InternetAddress {

  private final String ADDRESS_PATTERN =
         "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}"
        + "(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
  private final String MASK_PATTERN = "\\/(([1-9])|([1-2]?[0-9])|([3]?[0-2]))"; 
  private int defaultMaskBits;
  private int maskBits;
  private int[] addressOctets;
  private Scanner input;
  private String networkClass;

  /**
   * Constructor for the {@code InternetAddress} class.
   */
  public InternetAddress() {
    addressOctets = new int[4];
    defaultMaskBits = 0;
    input = new Scanner(System.in);
    maskBits = 0;
    networkClass = "";
  }

  /**
   * Prompts continuously for a valid IPv4 address, matching for a pattern.
   * Address can be entered in CIDR notation, or if not, calls for the 
   * {@code enterMaskBits} method to enter the subnet mask. Checks
   * the resulting address, and lastly calls the {@code verifyNetworkClass}
   * to check if a valid subnet mask for the address is entered.
   */
  public void enterIPv4() {
    boolean inputFlag = false;
    String[] addressParts = new String[2];
    do {
      inputFlag = false;
      System.out.print("Enter IPv4 address: ");
      try {
        String address = input.nextLine().trim();
        if (!((address.matches(ADDRESS_PATTERN))
            || address.matches(ADDRESS_PATTERN + MASK_PATTERN))) {
          throw new IllegalArgumentException("Invalid address. ");
        } // matches the pattern for CIDR or IP notation
        addressParts = address.split("\\/"); // splits the mask bits (if present)
        if (addressParts[0].equals(address)) { // checks if only IP address is entered
          address += enterMaskBits(); // prompts for subnet mask and adds to address
          addressParts = address.split("\\/"); // now splits the address from the mask bits
        }
        if (!address.matches(ADDRESS_PATTERN + MASK_PATTERN)
            || addressParts[1].equals("0")) { // mask bit greater than 0
          throw new IllegalArgumentException("Invalid address. ");
        } // matches the resulting string in CIDR notation
      } catch (IllegalArgumentException e) {
        inputFlag = true;
        System.out.print(String.format("\033[%dA", 1) + "\033[2K"); // restore line
        System.err.print(e.getMessage());
      }
    } while (inputFlag);
    maskBits = Integer.parseInt(addressParts[1]); // initialize the mask bits
    String[] addressOctets = addressParts[0].split("\\."); // splits the four octets
    for (int i = 0;  i < addressOctets.length; i++) {
      this.addressOctets[i] = Integer.parseInt(addressOctets[i]);
    } // initialize the array with four octets
    verifyNetworkClass(); // call to check the network class
  }

  /**
   * Prompts continuously for a valid subnet mask, converting to binary and
   * checking if all starting 1s are followed by all ending 0s.
   *
   * @return number of mask bits
   */
  private String enterMaskBits() {
    boolean inputFlag = true;
    int maskBits = 0;
    BinaryConverter convert = new BinaryConverter();
    do {
      String addressBits = "";
      inputFlag = false;
      System.out.print("Enter Subnet Mask: ");
      try {
        String address = input.nextLine().trim();
        String[] addressParts = address.split("\\.");
        if (!address.matches(ADDRESS_PATTERN)) {
          throw new IllegalArgumentException("Invalid mask. ");
        } // matches the subnet mask pattern
        for (int i = 0; i < addressParts.length; i++) { // convert to binary
          addressBits += convert.decimalToBinary(Integer.parseInt(addressParts[i]), 8);
        }
        char[] bitsArray = addressBits.toCharArray();
        int i = 0;
        while (bitsArray[i++] == ('1')) {
          maskBits++; // counts the mask bits
        }
        for (int j = i; j < bitsArray.length; j++) { // *
          if (bitsArray[j] == ('1')) {
            maskBits = 0; // discards invalid subnet mask
            throw new IllegalArgumentException("Invalid mask. ");
          }
        }
      } catch (IllegalArgumentException e) {
        inputFlag = true;
        System.out.print(String.format("\033[%dA", 1) + "\033[2K");
        System.err.print(e.getMessage());
      }
    } while (inputFlag);
    return "/" + maskBits;
  }

  /**
   * Returns the four octets of an IP address in an array.
   *
   * @return octets of an IP address
   */
  public int[] getAddressOctets() {
    return addressOctets;
  }

  /**
   * Returns the value of default subnet mask bits.
   *
   * @return the DSM bits
   */
  public int getDefaultMaskBits() {
    return defaultMaskBits;
  }

  /**
   * Returns the mask bits number.
   *
   * @return the mask bits number
   */
  public int getMaskBits() {
    return maskBits;
  }


  /**
   * Returns the class of the network.
   *
   * @return the network class
   */
  public String getNetworkClass() {
    return networkClass;
  }

  /**
   * Calculates the network class by looking at the first octet. Also sets the
   * DSM bits accordingly.
   */
  private void networkClass() {
    if (addressOctets[0] <= 126) {
      networkClass = "Class A";
      defaultMaskBits = 8;
    }
    if (addressOctets[0] == 127) {
      networkClass = "Class A Reserved Loopback Address";
    }
    if (addressOctets[0] >= 128 && addressOctets[0] <= 191) {
      networkClass = "Class B";
      defaultMaskBits = 16;
    }
    if (addressOctets[0] >= 192 && addressOctets[0] <= 223) {
      networkClass = "Class C";
      defaultMaskBits = 24;
    }
    if (addressOctets[0] >= 224 && addressOctets[0] <= 239) {
      networkClass = "Class D";
    }
    if (addressOctets[0] >= 240 && addressOctets[0] <= 255) {
      networkClass = "Class E";
    }
  }

  /**
   * Sets the network class back to an empty string.
   */
  private void reset() {
    networkClass = "";
  }

  /**
   * Checks if the value of the mask bits is within the defined ranges of its network class.
   * If out-of-range for classes A, B, and C, a message is displayed, the network class
   * and default subnet mask calculations are reset, and the input is prompted again.
   * Ranges of classes D and E are undefined, so the last process is repeated, but its
   * calculations will never be accepted.
   */
  private void verifyNetworkClass() {
    networkClass();
    if (networkClass.equals("Class A")) {
      if (maskBits < 8 || maskBits > 30) {
        System.out.println("Mask bits out of range for class A: [8-30].\n");
        reset();
        enterIPv4();
      }
    }
    if (networkClass.equals("Class B")) {
      if (maskBits < 16 || maskBits > 30) {
        System.out.println("Mask bits out of range for class B: [16-30].\n");
        reset();
        enterIPv4();
      }
    }
    if (networkClass.equals("Class C")) {
      if (maskBits < 24 || maskBits > 30) {
        System.out.println("Mask bits out of range for class C: [24-30].\n");
        reset();
        enterIPv4();
      }
    }
    if (networkClass.equals("Class D")) {
      System.out.println(networkClass + "\n");
      reset();
      enterIPv4();
    }
    if (networkClass.equals("Class E")) {
      System.out.println(networkClass + "\n");
      reset();
      enterIPv4();
    }
  }
}