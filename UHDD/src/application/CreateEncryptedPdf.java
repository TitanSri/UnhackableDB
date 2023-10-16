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
                document.add(new Paragraph("Patient Id: " + resultSet.getInt("patientId")));
                document.add(new Paragraph("Gender: " + resultSet.getString("gender")));
                document.add(new Paragraph("First Name: " + resultSet.getString("firstName")));
                document.add(new Paragraph("Middle Name: " + resultSet.getString("middleName")));
                document.add(new Paragraph("Last Name: " + resultSet.getString("lastName")));
                document.add(new Paragraph("Date of Birth: " + resultSet.getString("dateOfBirth")));
                document.add(new Paragraph("Address: " + resultSet.getString("address")));
                document.add(new Paragraph("City: " + resultSet.getString("city")));
                document.add(new Paragraph("State: " + resultSet.getString("state")));
                document.add(new Paragraph("Tele: " + resultSet.getString("telephone")));
                document.add(new Paragraph("Email: " + resultSet.getString("email")));
                
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
