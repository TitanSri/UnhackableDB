package application;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

/**
 * This uses the google xing library for qrcode
 * This is used in the user creation and credential manager
 * @author Team 5
 *
 */
public class OTPService {

    private static final int OTP_VALIDITY_SECONDS = 30;
    private static final int OTP_LENGTH = 6;
    private final TimeBasedOneTimePasswordGenerator totpGenerator;

    public OTPService() throws NoSuchAlgorithmException {
        TimeBasedOneTimePasswordGenerator tmp = null;
        try {
            tmp = new TimeBasedOneTimePasswordGenerator(java.time.Duration.ofSeconds(OTP_VALIDITY_SECONDS), OTP_LENGTH);
        } finally {
            totpGenerator = tmp;
        }
    }
    
    /**
     * This is used in the user creation 
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String generateSecretKey() throws NoSuchAlgorithmException {
        // generate a random key
        byte[] secretKey = new byte[20];
        new SecureRandom().nextBytes(secretKey);

        // encode key in base32
        Base32 base32 = new Base32();
        String base32Key = base32.encodeToString(secretKey);

        // remove padding characters
        base32Key = base32Key.replace("=", "");

        return base32Key;

    }
    
    /**
     * This is used in the user creation
     * @param secretKey
     * @param accountName
     * @param issuer
     * @return
     * @throws WriterException
     */
    public BufferedImage generateQRCode(String secretKey, String accountName, String issuer) throws WriterException {
        String otpAuthTotpUrl = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, accountName, secretKey, issuer);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthTotpUrl, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * This is used in the credential manager
     * @param secretKey
     * @param otp
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public boolean validateOTP(String secretKey, int otp) throws NoSuchAlgorithmException, InvalidKeyException {
        Base32 base32 = new Base32();
        byte[] secretKeyBytes = base32.decode(secretKey);
        SecretKey originalKey = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "AES");
        int expectedOTP = totpGenerator.generateOneTimePassword(originalKey, Instant.now());
        return expectedOTP == otp;
    }
}
