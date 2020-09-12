package src.calculator;

import java.util.Scanner;

/**
 * The {@code Driver} class contains static methods to calculate and display
 * the results of the subnetting calculation of an IPv4 address and subnet mask,
 * and to prompt if the calculation wants to be repeated.
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
public class Driver {

  private static Scanner input = new Scanner(System.in);

  /**
   * Prompts continuously for a yes/no until entered.
   *
   * @return true if entered yes, false if entered no
   */
  public static boolean goAgain() {
    boolean inputFlag = false;
    boolean choiceFlag = true;
    do { // prompt if taking input again
      choiceFlag = true;
      System.out.print("Go again? (y/n): ");
      try {
        char choice = Character.toLowerCase(input.nextLine().trim().charAt(0));
        if (choice == 'y' || choice == 'n') {
          choiceFlag = false;
          if (choice == 'y') {
            inputFlag = true;
          }
          if (choice == 'n') {
            inputFlag = false;
          }
        } else {
          throw new IllegalArgumentException();
        }
      } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
        choiceFlag = true;
        System.out.print(String.format("\033[%dA", 1) + "\033[2K");
        System.err.print("Invalid. ");
      }
      if (inputFlag) {
        System.out.println();
      }
    } while (choiceFlag);   
    return inputFlag; 
  }

  /**
   * Instantiates classes, makes calls to enter and calculate, and displays
   * the results by converting binary values of the getters to decimal.
   */
  public static void printResults() {
    InternetAddress address = new InternetAddress();
    address.enterIPv4();

    SubnetCalculator subnet = new SubnetCalculator(address);
    subnet.defaultSubnetMask();
    subnet.subnetMask();
    subnet.subnetId();    
    subnet.subnetIndex();
    subnet.wildcardMask();

    String subnetId = subnet.getSubnetId();
    int borrowedBits = subnet.getBorrowedBits();
    int hostBits = subnet.getHostBits();
    AddressRange range = new AddressRange(subnetId, borrowedBits, hostBits);

    range.networkPart();
    range.hostPart();
    range.subnetPart();
    range.broadcastAddress();
    range.firstHostAddress();
    range.lastHostAddress();

    String broadcastAddress = range.getBroadcastAddress();
    String firstHostAddress = range.getFirstHostAddress();
    String lastHostAddress = range.getLastHostAddress();

    subnet.subnetBitmap();

    StringBuffer buffer = new StringBuffer();
    BinaryConverter convert = new BinaryConverter();

    buffer
      .append("\nNetwork Class:\t\t\t")
      .append(address.getNetworkClass())
      .append("\nDefault Subnet Mask:\t\t")
      .append(convert.binaryToAddress(subnet.getDefaultSubnetMask()))
      .append("\nSubnet Mask:\t\t\t")
      .append(convert.binaryToAddress(subnet.getSubnetMask()))
      .append("\nWildcard Mask:\t\t\t")
      .append(convert.binaryToAddress(subnet.getWildcardMask()))
      .append("\nSubnet ID:\t\t\t")
      .append(convert.binaryToAddress(subnetId))
      .append("\nBroadcast Address:\t\t")
      .append(convert.binaryToAddress(broadcastAddress))
      .append("\nFirst Host Address:\t\t")
      .append(convert.binaryToAddress(firstHostAddress))
      .append("\nLast Host Address:\t\t")
      .append(convert.binaryToAddress(lastHostAddress))
      .append("\nSID Index:\t\t\t")
      .append(subnet.getSubnetIndex())
      .append("\nMask Bits:\t\t\t")
      .append(address.getMaskBits())
      .append("\nSubnet Bits:\t\t\t")
      .append(subnet.getBorrowedBits())
      .append("\nMaximum Subnets:\t\t")
      .append(String.format("%.0f", Math.pow(2, subnet.getBorrowedBits())))
      .append("\nHosts per Subnet:\t\t")
      .append(String.format("%.0f", Math.pow(2, subnet.getHostBits()) - 2))
      .append(String.format("\nHost Address Range:\t\t%s - %s",
          convert.binaryToAddress(firstHostAddress),
          convert.binaryToAddress(lastHostAddress)))
      .append("\nSubnet Bitmap:\t\t\t")
      .append(subnet.getSubnetBitmap())
      .append("\n");

    System.out.println(buffer);

    PossibleNetworks possibleNetworks =
        new PossibleNetworks(subnetId, borrowedBits, hostBits);
    possibleNetworks.enterIndex();
    possibleNetworks.possibleSubnets();
    possibleNetworks.possibleNetworkAddresses();
    possibleNetworks.possibleBroadcastAddresses();
    possibleNetworks.possibleUsableAddresses();
    possibleNetworks.printPossibleNetworks();
  }

  /**
   * Start of the main method.
   *
   * @param args arguments of the command line
   */
  public static void main(String[] args) {
    do {
      printResults();
    } while (goAgain());
  }
}
