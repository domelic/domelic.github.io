package src.calculator;

import java.util.Scanner;

/**
 * The {@code PossibleNetworks} class contains methods to enter the index range
 * for the subnets, methods to calculate the BA, FHIP, LHIP, and SID, and finally
 * to display the subnets in table format.
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
public class PossibleNetworks {

  private AddressRange range;
  private BinaryConverter convert;
  private int borrowedBits;
  private int firstIndex;
  private int hostBits;
  private int lastIndex;
  private int subnetIndex;
  private String subnetId;
  private String[] possibleBroadcastAddresses;
  private String[] possibleFirstHostAddresses;
  private String[] possibleLastHostAddresses;
  private String[] possibleNetworkAddresses;
  private String[] possibleSubnets;

  /**
   * Constructor for the {@code PossibleNetworks} class.
   *
   * @param subnetId the subnet ID in 32-bit format
   * @param borrowedBits the number of borrowed bits
   * @param hostBits the number of host bits
   */
  public PossibleNetworks(String subnetId, int borrowedBits, int hostBits) {
    convert = new BinaryConverter();
    firstIndex = 0;
    lastIndex = 0;
    possibleBroadcastAddresses = new String[0];
    possibleFirstHostAddresses = new String[0];
    possibleLastHostAddresses = new String[0];
    possibleNetworkAddresses = new String[0];
    possibleSubnets = new String[0];
    range = new AddressRange(subnetId, borrowedBits, hostBits);
    subnetIndex = (int) Math.pow(2, borrowedBits);
    this.borrowedBits = borrowedBits;
    this.hostBits = hostBits;
    this.subnetId = subnetId;
  }

  /**
   * Prompts for the first and last subnet index.
   */
  public void enterIndex() {
    System.out.printf("Index range: 0 - %d\n\n", subnetIndex - 1);
    boolean sizeFlag = false;
    do {
      sizeFlag = false;
      firstIndex = index("first");
      lastIndex = index("last");
      if (firstIndex > lastIndex) {
        System.out.print(String.format("\033[%dA", 1) + "\033[2K");
        System.out.print(String.format("\033[%dA", 1) + "\033[2K");
        System.out.print(String.format("\033[%dA", 1) + "\033[2K");
        System.out.println("Invalid index range.");
        sizeFlag = true;
      }
    } while (sizeFlag);
  }

  /**
   * Prompts for an index as an {@code int} value.
   *
   * @param pos a {@code String} indicating first or last index
   * @return the index that's entered
   */
  public int index(String pos) {
    Scanner input = new Scanner(System.in);
    boolean enterFlag = false;
    int index = 0;
    do {
      System.out.printf("Enter %s index: ", pos);
      enterFlag = false;
      try {
        index = Integer.parseInt(input.nextLine().trim());
        if (index < 0 || index >= subnetIndex) {
          throw new IllegalArgumentException();
        }
      } catch (IllegalArgumentException e) {
        enterFlag = true;
        System.out.print(String.format("\033[%dA", 1) + "\033[2K");
        System.err.print("Invalid index. ");
      }
    } while (enterFlag);
    return index;
  }

  /**
   * Calculates the possible broadcast addresses.
   */
  public void possibleBroadcastAddresses() {
    possibleBroadcastAddresses = new String[possibleSubnets.length];
    for (int i = 0; i < possibleNetworkAddresses.length; i++) {
      AddressRange range =
          new AddressRange(possibleNetworkAddresses[i], borrowedBits, hostBits);
      range.broadcastAddress();
      possibleBroadcastAddresses[i] = range.getBroadcastAddress();
    }
  }

  /**
   * Calculates the possible network addresses.
   */
  public void possibleNetworkAddresses() {
    possibleNetworkAddresses = new String[possibleSubnets.length];
    range.networkPart();
    range.hostPart();
    String networkPart = range.getNetworkPart();
    String hostPart = range.getHostPart();
    for (int i = 0; i < possibleNetworkAddresses.length; i++) {
      AddressRange range =
          new AddressRange(possibleSubnets[i], borrowedBits, hostBits);
      possibleNetworkAddresses[i] = networkPart + possibleSubnets[i] + hostPart;
      for (int j = possibleNetworkAddresses[0].length(); j < 32; j++) {
        possibleNetworkAddresses[0] += "0";
      }
    }
  }

  /**
   * Calculates the possible subnets.
   */
  public void possibleSubnets() {
    possibleSubnets = new String[lastIndex - firstIndex + 1];
    String firstSubnet = "";
    for (int i = 0; i < range.getSubnetPart().length(); i++) {
      firstSubnet += "0";
    }
    for (int i = 0, index = firstIndex; i < possibleSubnets.length; i++, index++) {
      possibleSubnets[i] = convert.binaryAddition(firstSubnet, index, borrowedBits);
    }
  }

  /**
   * Calculates the possible usable host addresses (FHIP and LHIP).
   */
  public void possibleUsableAddresses() {
    possibleFirstHostAddresses = new String[possibleSubnets.length];
    possibleLastHostAddresses = new String[possibleSubnets.length];
    for (int i = 0; i < possibleNetworkAddresses.length; i++) {
      AddressRange range =
          new AddressRange(possibleNetworkAddresses[i], borrowedBits, hostBits);
      range.networkPart();
      range.hostPart();
      range.subnetPart();
      range.firstHostAddress();
      range.lastHostAddress();
      possibleFirstHostAddresses[i] = range.getFirstHostAddress();
      possibleLastHostAddresses[i] = range.getLastHostAddress();
    }
  }

  /**
   * Prints the subnets in table format.
   */
  public void printPossibleNetworks() {
    StringBuffer buffer = new StringBuffer();
    String header = "";
    for (int i = 0; i < 75; i++) {
      header += "-";
    }
    buffer
      .append(header)
      .append("\n")
      .append("| ")
      .append(String.format("%15s", "Network Address"))
      .append(" | ")
      .append("        ")
      .append("Usable Host Range")
      .append("        ")
      .append(" | ")
      .append(String.format("%-17s", "Broadcast Address"))
      .append(" |")
      .append("\n");
    buffer
      .append(header)
      .append("\n");
    for (int i = 0; i < possibleSubnets.length; i++) {
      buffer
        .append("| ")
        .append(String.format("%15s",
         convert.binaryToAddress(possibleNetworkAddresses[i])))
        .append(" | ")
        .append(String.format("%15s",
         convert.binaryToAddress(possibleFirstHostAddresses[i])))
        .append(" - ")
        .append(String.format("%-15s",
         convert.binaryToAddress(possibleLastHostAddresses[i])))
        .append(" | ")
        .append(String.format("%-17s",
         convert.binaryToAddress(possibleBroadcastAddresses[i])))
        .append(" |")
        .append("\n");
    }
    buffer
      .append(header)
      .append("\n");
    System.out.println(buffer);
  }
}