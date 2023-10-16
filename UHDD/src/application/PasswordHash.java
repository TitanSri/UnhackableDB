package application;

import org.bouncycastle.crypto.params.Argon2Parameters;
import java.util.Base64;

/**
 * This class is used in the password hasher, and credential manager
 * @author User
 *
 */
public class PasswordHash {
    private byte[] hash;
    private Argon2Parameters params;

    public PasswordHash(byte[] hash, Argon2Parameters params) {
        this.hash = hash;
        this.params = params;
    }

    /**
     * This used in the password hasher
     * @return
     */
    public byte[] getHash() {
        return this.hash;
    }

    /**
     * This is used in the password hasher
     * @return
     */
    public Argon2Parameters getParams() {
        return this.params;
    }

    /**
     * This is used in the password hasher and credential manager
     * @return
     */
    public String getHashAsString() {
        return Base64.getEncoder().encodeToString(this.hash);
    }

    /**
     * This is used in the credential manager
     * @return
     */
    public String getParamsAsString() {
        return Base64.getEncoder().encodeToString(this.params.getSalt()) + ":" + this.params.getMemory() + ":" + this.params.getIterations();
    }

    /**
     * This is used in the credential manager
     * @param hash
     * @param params
     * @return
     */
    public static PasswordHash fromString(String hash, String params) {
        byte[] decodedHash = Base64.getDecoder().decode(hash);
        
        String[] splitParams = params.split(":");
        System.out.println(params);
        System.out.println(splitParams[0]);
        System.out.println(splitParams[1]);
        System.out.println(splitParams[2]);
         // Fixed here
        byte[] salt = Base64.getDecoder().decode(splitParams[0]);
        int memory = Integer.parseInt(splitParams[1]);
        int iterations = Integer.parseInt(splitParams[2]);
        

        
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withMemoryAsKB(memory)
                .withParallelism(4)
                .withIterations(iterations);

        Argon2Parameters paramsObj = builder.build();

        return new PasswordHash(decodedHash, paramsObj);
    }
}
