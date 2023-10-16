package application.viewControllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import com.google.zxing.WriterException;

import application.CredentialManager;
import application.EmailManager;
import application.OTPService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is used to control the user creation FXML/UI
 * @author User
 *
 */
public class UserCreationController {
	private Stage stage;
	private Scene scene;
	@FXML private Text actionGrabberCreator;
	@FXML private TextField userGrabberCreator;
	@FXML private TextField passGrabberCreator;
	@FXML private TextField emailGrabberCreator;
	@FXML private Rectangle ucRectanglePane;
	@FXML private ComboBox<String> cbRole;
	@FXML private ImageView qrCodeView;
	private OTPService otpService;

	static ObservableList<String> roles;
	private static final String regex = "^(.+)@(.+)$"; // ensure that it is a valid email address *@*
	Pattern specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE); // not a lower or upper case letter or number
    Pattern upperCasePattern = Pattern.compile("[A-Z ]"); // upper case regex
    Pattern lowerCasePattern = Pattern.compile("[a-z ]"); // lower case regex
    Pattern digitCasePattern = Pattern.compile("[0-9 ]"); // number regex
	
	public Image convertToFxImage(BufferedImage bufferedImage) {
		return SwingFXUtils.toFXImage(bufferedImage, null);
	}

	@FXML protected void handleCreateNewUsernAction(ActionEvent event) throws Exception  {
		System.out.println(cbRole.getValue());
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher= pattern.matcher(emailGrabberCreator.getText());
		System.out.println("Matcher: " + matcher);
		CredentialManager CredentialManager = new CredentialManager();
		boolean usernameExists = false;
		try {
			usernameExists = CredentialManager.doesUsernameExist(userGrabberCreator.getText());
		} catch (SQLException e) {
			e.printStackTrace();
			actionGrabberCreator.setText("Error checking username uniqueness");
			actionGrabberCreator.setFill(Color.RED);
			return; // Exit the method early due to the error
		}
		if(userGrabberCreator.getText().equals("") & passGrabberCreator.getText().equals("") & emailGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("All fields cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(userGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Username cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if (usernameExists){
			actionGrabberCreator.setText("Username already exists");
			actionGrabberCreator.setFill(Color.RED);
		} else if(passGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Password cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(checkPasswordRequirements(passGrabberCreator.getText()) < 3) {
			actionGrabberCreator.setText("Password has to be at least 8 to 64 characters. It requires 3 out of 4 of lowercase, uppercase, numbers, or symbols");
			actionGrabberCreator.setFill(Color.RED);
		} else if(emailGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Email cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(!(matcher.matches())) {
			actionGrabberCreator.setText("Email is not a valid email address");
			actionGrabberCreator.setFill(Color.RED);
		} else if(cbRole.getValue() == null) {
			actionGrabberCreator.setText("Role cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		}  
		else {
			//MFA OTP
			otpService = new OTPService();
			String secretKey = otpService.generateSecretKey();
			System.out.println("this is the secret key:  " + secretKey);
			BufferedImage qrCode = otpService.generateQRCode(secretKey, userGrabberCreator.getText(), "UHDB");
			/* 
			// Convert the QR code to an FX image - no longer required as we are sending the QR code as an attachment
			Image qrCodeFxImage = convertToFxImage(qrCode);
			qrCodeView.setImage(qrCodeFxImage);
			*/
			// Create a new task for sending email
			Task<Void> emailTask = new Task<Void>() {
				@Override
				protected Void call() throws MessagingException, Exception {
					EmailManager emailManager = new EmailManager();
					emailManager.sendEmailWithImage(emailGrabberCreator.getText(), "UHDB MFA QR Code", "Please scan the QR code to set up MFA", qrCode);
					return null;
				}
			};
			
			//Provide status for user
			actionGrabberCreator.setText("Email with MFA QR Code sending...");
			actionGrabberCreator.setFill(Color.GREEN);
			// Handle task completion or failure
			emailTask.setOnSucceeded(e -> {
				actionGrabberCreator.setText("User Creation Successful - check email for MFA setup");
				actionGrabberCreator.setFill(Color.GREEN);
				String userCreate = userGrabberCreator.getText();
				String passCreate = passGrabberCreator.getText();
				String emailCreate = emailGrabberCreator.getText();
				String roleCreate = cbRole.getValue();
				try {
					CredentialManager.addNewUserToDB(userCreate, passCreate, emailCreate, roleCreate, secretKey);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});

			emailTask.setOnFailed(e -> {
				actionGrabberCreator.setText("Failed to send email");
				actionGrabberCreator.setFill(Color.RED);
			});

			// Start the task on a new thread
			new Thread(emailTask).start();
		}
	}
	
	public int checkPasswordRequirements(String password) {
		int passwordCount = 0;
		if (password.length() >= 8) {
			if (upperCasePattern.matcher(password).find()) {
				passwordCount++;
			}
			if (lowerCasePattern.matcher(password).find()) {
				passwordCount++;
			}
			if (specialCharPattern.matcher(password).find() ) {
				passwordCount++;
			}
			if (digitCasePattern.matcher(password).find()) {
				passwordCount++;
			}
		}
		
		System.out.println("Password count requirement: " + passwordCount);
		return passwordCount;
		
	}
	
	public void switchToHomepage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize() {
		roles = FXCollections.observableArrayList("Doctor", "Nurse");
		cbRole.getItems().addAll(roles);
		System.out.println(cbRole.getItems());
		
	}
}
