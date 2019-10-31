import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Implement DES (Data Encryption Standard) Algorithm
 */
public class Lab05 {
    public static void main(String[] args) {
        DES encryption = new DES();
        Scanner scanner = new Scanner(System.in);

        int count = scanner.nextInt();
        for (int i = 0; i < count; i++) {
            String message = scanner.next();
            String key = scanner.next();
            encryption.encrypt(message, key);
            System.out.println();
        }
        scanner.close();
    }
}

class DES {

    public String encrypt(String message, String key) {
        System.out.println("Message: " + message);
        System.out.println("Key    : " + key);
        key = hexToBinary(key);
        key = permute(key, Constants.PARITY_BIT);

        String left = key.substring(0, key.length() / 2);
        String right = key.substring(key.length() / 2);

        ArrayList<String> rkb = new ArrayList<>(); // RoundKeys in binary
        ArrayList<String> rk = new ArrayList<>(); // RoundKeys in hexadecimal
        for (int i = 0; i < 16; i++) {
            // Shifting
            left = shiftLeft(left, Constants.SHIFT_TABLE[i]);
            right = shiftLeft(right, Constants.SHIFT_TABLE[i]);

            // Combining
            String combine = left + right;

            // Key Compression [48-bit Sub]
            String roundKey = permute(combine, Constants.KEY_COMPRESSION);
            rkb.add(roundKey);
            // rk.add(binaryToHex(roundKey));
        }

        String cipher = encrypt(message, rkb, rk);
        System.out.printf("[Encryption]\tCipher Text:\t%s\n", cipher);

        Collections.reverse(rkb);
        Collections.reverse(rk);
        String text = encrypt(cipher, rkb, rk);
        System.out.printf("[Decryption]\tPlain Text :\t%s\n", text);
        return cipher;
    }

    public String encrypt(String part, ArrayList<String> rkb, ArrayList<String> rk) {
        part = hexToBinary(part);
        part = permute(part, Constants.INITIAL_PERMUTATION);

        String left = part.substring(0, part.length() / 2);
        String right = part.substring(part.length() / 2);

        for (int i = 0; i < 16; i++) {
            String previousL = left;
            left = right;

            // Ki + E(Ri)
            String roundKeyXor = xor(rkb.get(i), permute(right, Constants.EXPANSION_D));
            String sTableResult = "";
            for (int j = 0; j < 8; j++) {
                sTableResult += getSTableValue(roundKeyXor.substring(6 * j, 6 * j + 6), j);
            }

            // f(Ki + E(Ri))
            right = xor(previousL, permute(sTableResult, Constants.STRAIGHT_PERMUTATION));
        }

        // Converted R16L16
        part = right + left;

        String binaryCipher = permute(part, Constants.FINAL_PERMUTATION);
        return binaryToHex(binaryCipher);
    }

    private String getSTableValue(String text, int sIndex) {
        int i = Integer.parseInt("" + text.charAt(0) + text.charAt(5), 2);
        int j = Integer.parseInt(text.substring(1, 5), 2);
        int num = Constants.S_BOX[sIndex][i][j];

        return String.format("%4s", Integer.toBinaryString(num)).replace(" ", "0");
    }

    private String permute(String k, int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(k.charAt(arr[i] - 1));
        }
        return sb.toString();
    }

    private String shiftLeft(String text, int shifts) {
        shifts = shifts % text.length();
        return text.substring(shifts) + text.substring(0, shifts);
    }

    private String xor(String a, String b) {
        StringBuilder sb = new StringBuilder(a.length());
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                sb.append("0");
            } else {
                sb.append("1");
            }
        }
        return sb.toString();
    }

    private String binaryToHex(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i += 4) {
            switch (s.substring(i, i + 4)) {
            case "0000":
                sb.append('0');
                break;
            case "0001":
                sb.append('1');
                break;
            case "0010":
                sb.append('2');
                break;
            case "0011":
                sb.append('3');
                break;
            case "0100":
                sb.append('4');
                break;
            case "0101":
                sb.append('5');
                break;
            case "0110":
                sb.append('6');
                break;
            case "0111":
                sb.append('7');
                break;
            case "1000":
                sb.append('8');
                break;
            case "1001":
                sb.append('9');
                break;
            case "1010":
                sb.append('A');
                break;
            case "1011":
                sb.append('B');
                break;
            case "1100":
                sb.append('C');
                break;
            case "1101":
                sb.append('D');
                break;
            case "1110":
                sb.append('E');
                break;
            case "1111":
                sb.append('F');
                break;

            default:
                break;
            }
        }
        return sb.toString();
    }

    private String hexToBinary(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
            case '0':
                sb.append("0000");
                break;
            case '1':
                sb.append("0001");
                break;
            case '2':
                sb.append("0010");
                break;
            case '3':
                sb.append("0011");
                break;
            case '4':
                sb.append("0100");
                break;
            case '5':
                sb.append("0101");
                break;
            case '6':
                sb.append("0110");
                break;
            case '7':
                sb.append("0111");
                break;
            case '8':
                sb.append("1000");
                break;
            case '9':
                sb.append("1001");
                break;
            case 'A':
            case 'a':
                sb.append("1010");
                break;
            case 'B':
            case 'b':
                sb.append("1011");
                break;
            case 'C':
            case 'c':
                sb.append("1100");
                break;
            case 'D':
            case 'd':
                sb.append("1101");
                break;
            case 'E':
            case 'e':
                sb.append("1110");
                break;
            case 'F':
            case 'f':
                sb.append("1111");
                break;

            default:
                break;
            }
        }
        if (sb.length() % 8 != 0) {
            sb.insert(0, "0000");
        }
        return sb.toString();
    }

    public String strToBinary(String str) {
        byte[] bytes = str.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        int pad = 8 - binary.length() % 8;
        while (pad > 0) {
            binary.insert(9, "0");
            pad--;
        }
        return binary.toString();
    }

    public String stringToHex(String str) {
        StringBuilder hex = new StringBuilder();
        for (char c : str.toCharArray()) {
            hex.append(String.format("%2H", c));
        }
        int pad = 16 - hex.length() % 16;
        while (pad > 0) {
            hex.append("0");
            pad--;
        }

        return hex.toString().replace(" ", "0");
    }

    public void printFormatted(String s, int breaks) {
        int i = 0;
        for (char c : s.toCharArray()) {
            System.out.print(c);
            i++;
            if (i % breaks == 0)
                System.out.print(' ');
        }
        System.out.println();
    }
}

class Constants {
    public static final int[] PARITY_BIT = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43,
            35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45,
            37, 29, 21, 13, 5, 28, 20, 12, 4 };

    public static final int[] SHIFT_TABLE = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    public static final int[] KEY_COMPRESSION = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16,
            7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36,
            29, 32 };

    public static final int[] INITIAL_PERMUTATION = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62,
            54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27,
            19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

    public static final int[] EXPANSION_D = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15,
            16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

    public static final int[][] S1 = { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
            { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
            { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
            { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } };

    public static final int[][] S2 = { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
            { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
            { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
            { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } };

    public static final int[][] S3 = { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
            { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
            { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
            { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } };

    public static final int[][] S4 = { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
            { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
            { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
            { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } };

    public static final int[][] S5 = { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
            { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
            { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
            { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } };

    public static final int[][] S6 = { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
            { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
            { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
            { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } };

    public static final int[][] S7 = { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
            { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
            { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } };

    public static final int[][] S8 = { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
            { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
            { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
            { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };

    public static final int[][][] S_BOX = { S1, S2, S3, S4, S5, S6, S7, S8 };

    public static final int[] STRAIGHT_PERMUTATION = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2,
            8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };

    public static final int[] FINAL_PERMUTATION = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6,
            46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19,
            59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };
}