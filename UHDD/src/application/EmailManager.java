//UHDB@lh3b.onmicrosoft.com
//Nap00764
package application;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

/**
 * This class is used for the user creation and UI
 * @author User
 *
 */
public class EmailManager {

//    private  String smtpHost = "smtp.office365.com";
    private  String smtpPort = "587";
//    private  String smtpUser = "UHDB@lh3b.onmicrosoft.com";
//    private  String smtpPassword = "Nap00764";
    
    private  String smtpHost = "smtp.gmail.com";
//    private  String smtpPassword = "**";
    private  String smtpUser = "csuuhdd@gmail.com";
    
    /**
     * This is used for the user creation 
     * @param to
     * @param subject
     * @param body
     * @param image
     * @throws MessagingException
     * @throws IOException
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws InvalidKeySpecException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public void sendEmailWithImage(String to, String subject, String body, BufferedImage image) throws MessagingException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
    	String smtpPassword = Decryptor.decrypt("uR1zA/cZCpYuPsEBE8b71wifDK29WIDrSg+8fgo/l10=");
    	
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        // alternative 
        properties.put("mail.smtp.ssl.trust", smtpHost);
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
        properties.put("mail.smtp.ssl.enable", "false");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUser));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart();

        // Add text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);
        multipart.addBodyPart(textPart);

        // Add image part
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource dataSource = new ByteArrayDataSource(convertBufferedImageToByteArray(image, "png"), "image/png");
        imagePart.setDataHandler(new DataHandler(dataSource));
        imagePart.setFileName("qrcode.png");
        imagePart.setHeader("Content-ID", "<qrcode>");
        multipart.addBodyPart(imagePart);

        message.setContent(multipart);

        Transport.send(message);
    }

    /**
     * This is only used in this class
     * @param image
     * @param format
     * @return
     * @throws IOException
     */
    private byte[] convertBufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }
}
