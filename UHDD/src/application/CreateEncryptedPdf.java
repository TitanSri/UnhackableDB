package application;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This will create a PDF containing the patients information and save it to the computer
 * This object is used in the Patient Info View controller for the patients details and medication PDF
 * @author Team 5
 *
 */
public class CreateEncryptedPdf {
	
	/**
	 * This will create a patient details pdf to a path file
	 * @param patientId
	 * @param finalPath
	 * @param ownerPassword
	 * @param userPassword
	 * @param patientName
	 * @throws Exception
	 */
    public static void createPatientDetailsPdf(String patientId, String finalPath, String ownerPassword, String userPassword, String patientName) throws Exception {
        DBConnector dbconnector = new DBConnector();
        dbconnector.initialiseDB();
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(finalPath));
            
            // Encrypt the PDF as it's being written
            writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            
            document.open();
            Paragraph heading = new Paragraph(patientName + "'s patient details");
            heading.getFont().setSize(18);
            heading.getFont().setStyle(Font.BOLD);
            heading.setAlignment(Element.ALIGN_LEFT);
            heading.setSpacingAfter(20);
            document.add(heading);

            // Fetch patient data
            ResultSet resultSet = dbconnector.QueryReturnResultsFromPatientDataId(patientId);
            if (resultSet.next()) {
	            String encryptionKey = resultSet.getString("EncryptionKey"); // Retrieve the encryption key
                document.add(new Paragraph("Patient Id: " +  resultSet.getInt("patientId")));
                document.add(new Paragraph("Gender: " + DataEncryptorDecryptor.decrypt(resultSet.getString("gender"), encryptionKey)));
                document.add(new Paragraph("First Name: " + DataEncryptorDecryptor.decrypt(resultSet.getString("firstName"), encryptionKey)));
                document.add(new Paragraph("Middle Name: " + DataEncryptorDecryptor.decrypt(resultSet.getString("middleName"), encryptionKey)));
                document.add(new Paragraph("Last Name: " + DataEncryptorDecryptor.decrypt(resultSet.getString("lastName"), encryptionKey)));
                document.add(new Paragraph("Date of Birth: " + DataEncryptorDecryptor.decrypt(resultSet.getString("dateOfBirth"), encryptionKey)));
                document.add(new Paragraph("Address: " + DataEncryptorDecryptor.decrypt(resultSet.getString("address"), encryptionKey)));
                document.add(new Paragraph("City: " + DataEncryptorDecryptor.decrypt(resultSet.getString("city"), encryptionKey)));
                document.add(new Paragraph("State: " + DataEncryptorDecryptor.decrypt(resultSet.getString("state"), encryptionKey)));
                document.add(new Paragraph("Tele: " + DataEncryptorDecryptor.decrypt(resultSet.getString("telephone"), encryptionKey)));
                document.add(new Paragraph("Email: " + DataEncryptorDecryptor.decrypt(resultSet.getString("email"), encryptionKey)));
                
            } else {
                document.add(new Paragraph("No data found for patient ID: " + patientId));
            }

            document.close();
            writer.close();
            dbconnector.closeConnection();
            
        } catch (DocumentException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will create a PDF containing the patient's medications  
     * @param patientId
     * @param finalPath
     * @param ownerPassword
     * @param userPassword
     * @param patientName
     * @throws Exception
     */
    public static void createMedicationPdf(String patientId, String finalPath, String ownerPassword, String userPassword, String patientName) throws Exception{
        DBConnector dbconnector = new DBConnector();
        dbconnector.initialiseDB();
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(finalPath));
            
            // Encrypt the PDF as it's being written
            writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            
            document.open();
            Paragraph heading = new Paragraph(patientName + "'s prescribed medication");
            heading.getFont().setSize(18);
            heading.getFont().setStyle(Font.BOLD);
            heading.setAlignment(Element.ALIGN_CENTER);
            heading.setSpacingAfter(20);
            document.add(heading);  
            // Fetch patient data
            ResultSet resultSet = dbconnector.QueryReturnResultsMedicationFromPatientId(patientId);
            while (resultSet.next()) {
                Paragraph subheading = new Paragraph("Medication Name: " + resultSet.getString("medication_name"));
                subheading.getFont().setSize(14);
                subheading.getFont().setStyle(Font.BOLD);
                document.add(subheading);
                document.add(new Paragraph("Patient Id: " + resultSet.getInt("patientId")));
                
                document.add(new Paragraph("Script Id: " + resultSet.getString("scriptId")));
                document.add(new Paragraph("Patient Id: " + resultSet.getString("patientId")));
                document.add(new Paragraph("Note Id: " + resultSet.getString("noteId")));
                document.add(new Paragraph("Doctor Id: " + resultSet.getString("prescribedBy")));
                document.add(new Paragraph("Date Prescribed: " + resultSet.getString("prescribed_date")));
                document.add(new Paragraph("Expiry Date: " + resultSet.getString("expired_date")));
            } 
            
            document.close();
            writer.close();
            dbconnector.closeConnection();
            
        } catch (DocumentException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
