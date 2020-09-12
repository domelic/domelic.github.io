package src.calculator;

import java.util.ArrayList;

/**
 * The {@code BinaryConverter} class contains static methods to convert a binary
 * number to decimal and vice versa, add (or subtract) a value to a binary number,
 * and to convert a 32-bit binary value to a 4-octet address. 
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
public class BinaryConverter {

  /**
   * Adds (or subtracts) a value to a binary number in any bit format.
   *
   * @param binary the binary number in any format
   * @param value the number to add (or subtract)
   * @param size the desired format length of the binary number
   * @return the binary number
   */
  public static String binaryAddition(String binary, int value, int size) {
    return decimalToBinary((binaryToDecimal(binary) + value), size);
  }

  /**
   * Converts a binary representation of an address to its decimal value
   * in the IP address format.
   *
   * @param binary address in binary
   * @return address in decimal
   */
  public static String binaryToAddress(String binary) {
    char[][] addressArray = new char[4][8];
    char[] bitsArray = binary.toCharArray();
    int counter = 0;
    for (int i = 0; i < addressArray.length; i++) {
      for (int j = 0; j < addressArray[i].length; j++) {
        addressArray[i][j] = bitsArray[counter++];
      } // populates the matrix with address bits
    }
    String[] octetArray = new String[4];
    for (int i = 0; i < addressArray.length; i++) {
      for (int j = 0; j < addressArray[i].length; j++) {
        octetArray[i] += addressArray[i][j];
      } // concatenate the bits in each octet
    }
    int[] decimalOctetArray = new int[4];
    for (int i = 0; i < decimalOctetArray.length; i++) {
      decimalOctetArray[i] = binaryToDecimal(octetArray[i]);
    } // converts each octet from binary to decimal
    String address = "";
    for (int i = 0; i < decimalOctetArray.length; i++) {
      address += Integer.toString(decimalOctetArray[i]);
      if (i != decimalOctetArray.length - 1) {
        address += ".";
      } // appends the octets with a dot in between
    }
    return address;
  }

  /**
   * Converts a binary number to its decimal representation.
   *
   * @param binary number
   * @return decimal number
   */
  public static int binaryToDecimal(String binary) {
    char[] binaryArray = binary.toCharArray();
    int decimalNumber = 0;
    for (int i = 0; i < binaryArray.length; i++) {
      if (binaryArray[i] == ('1')) {
        decimalNumber += Math.pow(2, binaryArray.length - i - 1);
      } // adds to the number the power in function of the base and position
    }
    return decimalNumber;
  }

  /**
   * Converts a decimal number to its 8-bit binary representation as a string.
   *
   * @param decimal decimal number to convert
   * @param size the desired format length of the binary number
   * @return the number of binary
   */
  public static String decimalToBinary(int decimal, int size) {
    String binaryBits = "";
    int remainder = 0;
    while (decimal != 0) { // while number is not spent
      remainder = decimal % 2; // gets the remainder of modulo 2
      decimal /= 2; // halves the number
      binaryBits += Integer.toString(remainder); // concatenates the remainder 
    }
    char[] bitsArray = binaryBits.toCharArray();
    char[] byteArray = new char[size];
    for (int i = 0; i < byteArray.length; i++) {
      try {
        byteArray[i] = bitsArray[i]; // populate the array
      } catch (ArrayIndexOutOfBoundsException e) {
        byteArray[i] = '0';
      }
    }
    String binaryNumber = "";
    for (int i = byteArray.length - 1; i >= 0; i--) {
      binaryNumber += byteArray[i]; // concatenate the indexes last to first
    }
    return binaryNumber;
  }
}