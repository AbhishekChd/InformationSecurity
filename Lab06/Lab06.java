import java.util.Scanner;

/**
 * Implement Diffie-Hellman Algorithm y^2 = x^3 + ax + b [Elliptic Curve]
 */
public class Lab06 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DiffieHellmanAlgorithm algorithm = new DiffieHellmanAlgorithm();

        int count = scanner.nextInt();

        for (int i = 0; i < count; i++) {
            long P = scanner.nextLong();
            long G = scanner.nextLong();

            long userAPrivateKey = scanner.nextLong();
            long userBPrivateKey = scanner.nextLong();

            System.out.printf("P = %d, G = %d\n", P, G);
            System.out.printf("User 1 Private Key: %d\t User 2 Private Key: %d\n", userAPrivateKey, userBPrivateKey);

            long userAKeyGenerated = algorithm.generateKey(P, G, userAPrivateKey);
            long userBKeyGenerated = algorithm.generateKey(P, G, userBPrivateKey);

            System.out.printf("User 1 Key Generated: %d\t User 2 Key Generated: %d\n", userAKeyGenerated,
                    userBKeyGenerated);

            long generatedSecretKeyA = algorithm.generateSecretKey(P, userBKeyGenerated, userAPrivateKey);
            long generatedSecretKeyB = algorithm.generateSecretKey(P, userAKeyGenerated, userBPrivateKey);

            System.out.printf("User 1 Secret Key: %d\t User 2 Secret Key: %d\n", generatedSecretKeyA,
                    generatedSecretKeyB);
            System.out.println();
        }

        scanner.close();
    }
}

class DiffieHellmanAlgorithm {

    public long generateKey(long p, long g, long privateKey) {
        // p = Prime, g = primitive root of p
        // Key Generated = (g^privateKey) % p
        return power(g, privateKey, p);
    }

    public long generateSecretKey(long p, long recievedKey, long privateKey) {
        // Secret Key Generated = (recievedKey^privateKey) % p
        return power(recievedKey, privateKey, p);
    }

    private long power(long a, long b, long mod) {
        long res = 1;
        for (long i = 0; i < b; i++) {
            res = (res * a) % mod;
        }
        return res;
    }
}