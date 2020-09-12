package src.calculator;

/**
 * The class {@code AddressRange} contains methods to calculate the broadcast
 * address, the first and last host addresses, as well as to export the
 * host, network and subnet parts of an IPv4 address.
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
public class AddressRange {

  private BinaryConverter convert;
  private int borrowedBits;
  private int hostBits;
  private String broadcastAddress;
  private String firstHostAddress;
  private String hostPart;
  private String lastHostAddress;
  private String networkPart;
  private String subnetId;
  private String subnetPart;

  /**
   * Constructor for the {@code AddressRange} class.
   *
   * @param subnetId the subnet ID in 32-bit format
   * @param borrowedBits the number of borrowed bits
   * @param hostBits the number of host bits
   */
  public AddressRange(String subnetId, int borrowedBits, int hostBits) {
    broadcastAddress = "";
    convert = new BinaryConverter();
    firstHostAddress = "";
    hostPart = "";
    lastHostAddress = "";
    networkPart = "";
    subnetPart = "";
    this.borrowedBits = borrowedBits;
    this.hostBits = hostBits;
    this.subnetId = subnetId;
  }

  /**
   * Calculates the broadcast address.
   */ 
  public void broadcastAddress() {
    char[] subnetArray = subnetId.toCharArray();
    for (int i = 0; i < subnetArray.length; i++) {
      if (subnetArray.length - i <= hostBits) {
        broadcastAddress += "1";
      } else {
        broadcastAddress += subnetArray[i];
      }
    }
  }

  /**
   * Calculates the first host address.
   */
  public void firstHostAddress() {
    String hostPart = "";
    char[] hostBits = this.hostPart.toCharArray();
    for (int i = 0; i < hostBits.length; i++) {
      hostPart += "0";
    }
    firstHostAddress =  
      networkPart
      + subnetPart
      + convert.binaryAddition(hostPart, 1, hostPart.length());
  }

  /**
   * Returns the broadcast address in 32-bit format.
   *
   * @return the broadcast address
   */
  public String getBroadcastAddress() {
    return broadcastAddress;
  }

  /**
   * Returns the first host address in 32-bit format.
   *
   * @return the first host address
   */
  public String getFirstHostAddress() {
    return firstHostAddress;
  }

  /**
   * Returns the host part of a 32-bit IP address.
   *
   * @return the address host part
   */
  public String getHostPart() {
    return hostPart;
  }

  /**
   * Returns the last host address in 32-bit format.
   *
   * @return the last host address
   */
  public String getLastHostAddress() {
    return lastHostAddress;
  }

  /**
   * Returns the network part of a 32-bit IP address.
   *
   * @return the address network part
   */
  public String getNetworkPart() {
    return networkPart;
  }

  /**
   * Returns the subnet part of a 32-bit IP address.
   *
   * @return the address subnets part
   */
  public String getSubnetPart() {
    return subnetPart;
  }

  /**
   * Calculates the host part of a 32-bit IP address.
   */
  public void hostPart() {
    for (int i = 32 - hostBits; i < 32; i++) {
      hostPart += subnetId.toCharArray()[i];
    }
  }

  /**
   * Calculates the last host address fo a 32-bit IP address.
   */
  public void lastHostAddress() {
    String hostPart = "";
    char[] hostBits = this.hostPart.toCharArray();
    for (int i = 0; i < hostBits.length; i++) {
      hostPart += "1";
    }
    lastHostAddress = 
        networkPart
      + subnetPart
      + convert.binaryAddition(hostPart, -1, hostPart.length());
  }

  /**
   * Calculates the network part of a 32-bit IP address.
   */
  public void networkPart() {
    for (int i = 0; i < 32 - borrowedBits - hostBits; i++) {
      networkPart += subnetId.toCharArray()[i];
    }
  }

  /**
   * Calculates the subnet part of a 32-bit IP address.
   */
  public void subnetPart() {
    for (int i = 32 - borrowedBits - hostBits; i < 32 - hostBits; i++) {
      subnetPart += subnetId.toCharArray()[i];
    }
  }
}