import java.util.Scanner;

/**
 * Implement biometrics to have physical security through different access
 * control permission
 */
public class Lab03 {
    public static void main(String[] args) {
        Encryption encryption = new Encryption();
        Scanner scanner = new Scanner(System.in);

        int count = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < count; i++) {
            String message = scanner.nextLine().toLowerCase();
            System.out.printf("Message: %s\n", message);

            String encrypted = encryption.encrypt(message);
            String decrypted = encryption.decrypt(encrypted);

            System.out.printf("Encrypted: %s\nDecrypted: %s\n\n", encrypted, decrypted);
        }

        scanner.close();
    }
}

class Encryption {
    public static final char p[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    public static final char ch[] = { 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H',
            'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M' };

    public String encrypt(String message) {
        char c[] = new char[(message.length())];
        for (int i = 0; i < message.length(); i++) {
            for (int j = 0; j < p.length; j++) {
                if (p[j] == message.charAt(i)) {
                    c[i] = ch[j];
                    break;
                }
            }
        }
        return (new String(c));
    }

    public String decrypt(String code) {
        char p1[] = new char[(code.length())];
        for (int i = 0; i < code.length(); i++) {
            for (int j = 0; j < ch.length; j++) {
                if (ch[j] == code.charAt(i)) {
                    p1[i] = p[j];
                    break;
                }
            }
        }
        return (new String(p1));
    }
}