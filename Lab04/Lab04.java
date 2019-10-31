import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Scanner;

/**
 * Implement RSA Algorithm
 */
public class Lab04 {
    public static void main(String[] args) {
        RSAEncryption encryption = new RSAEncryption();
        Scanner scanner = new Scanner(System.in);

        int count = scanner.nextInt();
        for (int i = 0; i < count; i++) {
            long prime1 = scanner.nextLong();
            long prime2 = scanner.nextLong();
            double message = scanner.nextDouble();

            System.out.printf("Message: %f\n", message);

            double encrypted = encryption.encrypt(prime1, prime2, message);
            double decrypted = encryption.decrypt(prime1, prime2, encrypted);

            System.out.printf("Encrypted: %f\nDecrypted: %f\n\n", encrypted, decrypted);
        }

        scanner.close();
    }
}

class RSAEncryption {
    public double encrypt(long prime1, long prime2, double message) {
        long n = prime1 * prime2;
        long[] keys = getKeys(prime1, prime2, n);
        long e = keys[0];
        return pow(message, e, n);
    }

    public double decrypt(long prime1, long prime2, double code) {
        long n = prime1 * prime2;
        long[] keys = getKeys(prime1, prime2, n);
        long d = keys[1];
        return pow(code, d, n);
    }

    public Map<Long, Long> getKeyPairs(long prime1, long prime2) {
        SortedMap<Long, Long> map = new TreeMap<>();

        long phi = (prime1 - 1) * (prime2 - 1);
        long e = 2;
        int k = 2;
        long d;

        while (e < phi) {
            if (gcd(e, phi) == 1) {
                d = (1 + (k * phi)) / e;
                map.put(e, d);
            }
            e++;
        }

        return map;
    }

    public long[] getKeys(long prime1, long prime2, long n) {
        long phi = (prime1 - 1) * (prime2 - 1);
        long e = 2;
        int k = 2;
        long d;

        while (e < phi) {
            if (e % n != 0 && gcd(e, phi) == 1) {
                d = (1 + (k * phi)) / e;
                return new long[] { e, d };
            }
            e++;
        }
        return new long[] {};
    }

    private long gcd(long a, long b) {
        long temp;
        while (true) {
            temp = a % b;
            if (temp == 0)
                return b;
            a = b;
            b = temp;
        }
    }

    private double pow(double a, long b, long mod) {
        double result = 1;
        for (long i = 0; i < b; i++) {
            result = (result * a) % mod;
        }
        return result;
    }
}
