package src.calculator;

/**
 * The {@code SubnetCalculator} class contains methods for calculating
 * subnetting information for an IPv4 address, those being the default subnet
 * mask, the subnet bitmap,  the subnet ID and index, the subnet mask, and
 * the wildcard mask, together with their respective getters.
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
public class SubnetCalculator {

  private int borrowedBits;
  private int subnetIndex;
  private InternetAddress address;
  private static BinaryConverter convert;
  private static int hostBits;
  private String defaultSubnetMask;
  private String subnetBitmap;
  private String subnetId;
  private String subnetMask;
  private String wildcardMask;

  /**
   * Constructor for the {@code SubnetCalculator} class.
   *
   * @param address the IPv4 address
   */
  public SubnetCalculator(InternetAddress address) {
    borrowedBits = 0;
    convert = new BinaryConverter();
    defaultSubnetMask = "";
    hostBits = 0;
    subnetBitmap = "";
    subnetId = "";
    subnetIndex = 0;
    subnetMask = "";
    this.address = address;
    wildcardMask = "";
  }

  /**
  * Calculates the default subnet mask in 32-bit format.
  */
  public void defaultSubnetMask() {
    for (int i = 0; i < 32; i++) {
      if (i < address.getDefaultMaskBits()) {
        defaultSubnetMask += "1";
      } else {
        defaultSubnetMask += "0";
      }
    }
  }

  /**
   * Returns the the number of borrowed bits.
   *
   * @return the number of borrowed bits
   */
  public int getBorrowedBits() {
    return borrowedBits;
  }

  /**
   * Returns the subnet mask as a 32-bit binary in {@code String} format.
   *
   * @return default subnet mask as 32-bit binary
   */
  public String getDefaultSubnetMask() {
    return defaultSubnetMask;
  }

  public int getHostBits() {
    return hostBits;
  }

  /**
   * Returns the subnet bitmap.
   *
   * @return the subnet bitmap
   */
  public String getSubnetBitmap() {
    return subnetBitmap;
  }

  /**
   * Returns 
   * Gets the value of the SID in binary as a string.
   *
   * @return SID in binary
   */
  public String getSubnetId() {
    return subnetId;
  }

  /**
   * Gets the subnet index number.
   *
   * @return the subnet index number
   */
  public int getSubnetIndex() {
    return subnetIndex;
  }

  /**
   * Gets the value of the subnet mask in binary as a string.
   *
   * @return the subnet mask in binary
   */
  public String getSubnetMask() {
    return subnetMask;
  }

  /**
   * Gets the value of the wildcard mask in binary as a string.
   *
   * @return the wildcard mask in binary
   */
  public String getWildcardMask() {
    return wildcardMask;
  }

  /**
   * Calculates the subnet bitmap as a fixed network identifier, followed
   * by the subnet identifier and host identifier respectively.
   */
  public void subnetBitmap() {
    String networkClass = address.getNetworkClass();
    if (networkClass.equals("Class A")) {
      subnetBitmap += "0nnnnnnn.";
    }
    if (networkClass.equals("Class B")) {
      subnetBitmap += "10nnnnnnn.nnnnnnnn.";
    }
    if (networkClass.equals("Class C")) {
      subnetBitmap += "110nnnnn.nnnnnnnn.nnnnnnnn.";
    }
    int i = 0;
    for (i = 0; i < borrowedBits; i++) {
      subnetBitmap += "s";
      if (i == 8 || i == 16) {
        subnetBitmap += ".";
      }
    }
    for (int j = 0; j < hostBits; j++) {
      if (i + j == 8 || i + j == 16) {
        subnetBitmap += ".";
      }
      subnetBitmap += "h";
    }
  }

  /**
   * Calculates the SID by binary ANDing the subnet mask and default subnet
   * mask.
   */
  public void subnetId() {
    String addressInBinary = "";
    int[] addressOctets = address.getAddressOctets();
    for (int i = 0; i < addressOctets.length; i++) {
      addressInBinary += convert.decimalToBinary(addressOctets[i], 8);
    }
    char[] bitsArray = addressInBinary.toCharArray();
    char[] subnetBits = subnetMask.toCharArray();
    for (int i = 0; i < bitsArray.length; i++) {
      if (bitsArray[i] == '1' && subnetBits[i] == '1') {
        subnetId += "1";
      } else {
        subnetId += "0";
      }
    }
  }

  /**
   * Calculates the subnet index as a function of the absolute position of the
   * borrowed bits of the subnet mask in the SID, and converting those bits in
   * the SID to decimal where its positions are reset.
   */
  public void subnetIndex() {
    char[] defaultSubnetArray = defaultSubnetMask.toCharArray();
    char[] subnetArray = subnetId.toCharArray();
    char[] maskArray = subnetMask.toCharArray();
    boolean bitsFlag = true;
    int binaryCount = 0;
    for (int i = 0; i < defaultSubnetArray.length; i++) {
      if (defaultSubnetArray[i] == ('1')) {
        binaryCount++;
      } // counts the number of 1 bits in the DSM
    }
    String subnetIndexBits = "";
    for (int i = binaryCount; i < subnetArray.length; i++) { // count from first 0 in DSM
      if (maskArray[i] == '1' || subnetArray[i] == '1') { // 1s in SM after last 1 in DSM
        borrowedBits++; // calculates the number of borrowed bits
        subnetIndexBits += subnetArray[i]; // concatenates the borrowed bits
      } else {
        hostBits++;
      } // calculates the host bits as the portion after the borrowed bits
    }
    subnetIndex = convert.binaryToDecimal(subnetIndexBits); // converts the index to decimal
  }

  /**
   * Calculates the subnet mask by adding 1s until the mask bit are exhausted,
   * and filling in the rest with 0s.
   */
  public void subnetMask() {
    for (int i = 0; i < 32; i++) {
      if (i < address.getMaskBits()) {
        subnetMask += "1";
      } else {
        subnetMask += "0";
      }
    }
  }

  /**
   * Calculates the wildcard mask by flipping the bits of the subnet mask.
   */
  public void wildcardMask() {
    char[] maskBits = subnetMask.toCharArray();
    for (int i = 0; i < maskBits.length; i++) {
      if (maskBits[i] == '1') {
        wildcardMask += '0';
      }
      if (maskBits[i] == '0') {
        wildcardMask += '1';
      }
    }
  }
}