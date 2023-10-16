package application;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

/**
 * This class is used in the credential manager
 * @author User
 *
 */
public class PasswordHasher {
    private SecureRandom random;
    private Argon2BytesGenerator gen;

    public PasswordHasher() {
        this.random = new SecureRandom();
        this.gen = new Argon2BytesGenerator();
    }

    /**
     * This is used in the credential manager 
     * @param password
     * @return
     */
    public PasswordHash hashPassword(String password) {
        // Generate a secure random salt
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Configure the Argon2 parameters
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(4)
                .withMemoryAsKB(65536) // use 64 MB
                .withIterations(3);

        Argon2Parameters params = builder.build();

        // Hash the password
        gen.init(params);
        byte[] hash = new byte[32]; // 256-bit hash
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), hash);
        return new PasswordHash(hash, params);
    }

    /**
     * This is used inthe password reset and the credential manager
     * @param password
     * @param passwordHash
     * @return
     */
    public boolean verifyPassword(String password, PasswordHash passwordHash) {
        gen.init(passwordHash.getParams());
        byte[] inputHash = new byte[32]; // 256-bit hash
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), inputHash);
        
        System.out.println(passwordHash.getHash());
        System.out.println(inputHash);        
        return Arrays.equals(passwordHash.getHash(), inputHash);
    }
}
