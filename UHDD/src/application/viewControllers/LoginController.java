package application.viewControllers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import application.CredentialManager;
import application.DBConnector;
import application.LoginResult;
import application.Patient;
import application.PatientService;
import application.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is used to control the login FXML/UI
 * @author User
 *
 */
public class LoginController {
    private Stage stage;
    private Scene scene;
    
    private CredentialManager credentialManager;
    private static String currentUser;

    @FXML
    private TextField userGrabber;

    @FXML
    private TextField passGrabber;

    @FXML
    private Text actionGrabber;

    @FXML
    private Button btnLogin;

    DBConnector dbConnector = new DBConnector();
    String currentFXML;
    UserSession us = new UserSession();
    

    public boolean loginSuccessful() throws Exception {
        credentialManager = new CredentialManager();
        String userLog = userGrabber.getText();
        String passLog = passGrabber.getText();
        String emailTo = credentialManager.verifyPasswordAndReturnEmail(userLog, passLog);
        ResultSet userRole = dbConnector.QueryReturnResultsForUserSession(userLog);
        if(userRole.next()){
            String id = userRole.getString("id");
            String role = userRole.getString("role");
            UserSession.initInstance(userLog,role,id);
        }

        //Check if password last set date was more than 30 days ago - returns true if password has expired
        if(!credentialManager.checkPasswordLastSetDate(userLog)){
            if (emailTo == null) {
                actionGrabber.setText("No user match found");
                actionGrabber.setFill(Color.RED);
                return false;
            }

            //2FA verification
            LoginResult result = credentialManager.verifyMFA(userLog);

            if (result == LoginResult.SUCCESSFUL) {
                // Handle successful login
                return true;
            } else if (result == LoginResult.WRONG_CODE) {
                // Handle wrong code scenario
                actionGrabber.setText("Wrong code input. MFA verification cancelled");
                actionGrabber.setFill(Color.RED);
                return false;
            } else if (result == LoginResult.CANCELLED) {
                // Handle login cancelled scenario
                actionGrabber.setText("MFA verification cancelled");
                actionGrabber.setFill(Color.RED);
                return false;
            } else {
                // Handle any other result if necessary
                return false;
            }
        } else {
            actionGrabber.setText("Password has expired. Please reset your password");
            //Open popup window
            Stage popupStage = new Stage();
            Parent popupRoot = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/PopUpPwdExpired.fxml"));
            Scene popupScene = new Scene(popupRoot);
            popupStage.setScene(popupScene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
            return false;
        }
    }

    
    public void updatePatientInMem(int id) throws Exception{
		dbConnector.initialiseDB();
		ResultSet patientDetails = dbConnector.QueryReturnResultsFromPatientDataId(String.valueOf(id));
		Patient patient;
		while(patientDetails.next()){
			String familyName = patientDetails.getString("lastName");
			String givenName = patientDetails.getString("firstName");
			String middleName = patientDetails.getString("middleName");
			String gender = patientDetails.getString("gender");
			String address = patientDetails.getString("address");
			String city = patientDetails.getString("city");
			String state = patientDetails.getString("state");
			String telephone = patientDetails.getString("telephone");
			String email = patientDetails.getString("email");
			String dateOfBirth = patientDetails.getString("dateOfBirth");
			String healthInsuranceNumber = patientDetails.getString("healthInsuranceNumber");
			String emergencyContactNumber = patientDetails.getString("emergencyContactNumber");
			patient = new Patient(id, familyName, givenName, middleName, gender, address, city, state, telephone, email, dateOfBirth, healthInsuranceNumber, emergencyContactNumber);
			PatientService.getInstance().setCurrentPatient(patient);
		}
	}

    @FXML public void initialize() throws Exception{
        updatePatientInMem(1);
    }
    
    @FXML
    protected void handlePwdResetAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/PasswordReset.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML 
    public void onMouseClickedCancelBtn(ActionEvent event) {
    	Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void handleSignInAction(ActionEvent event) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY hh:mm:ss a");
        String formattedDateTime = currentDateTime.format(formatter);
        DBConnector dbConnector = new DBConnector();
        dbConnector.initialiseDB();

        String username = userGrabber.getText();
        us.setUserName(username);
        System.out.println("Username: " + us.getUserName());
        int loggedInStatus = dbConnector.getLoggedInStatus(username);

        if (userGrabber.getText().isEmpty() && passGrabber.getText().isEmpty()) {
            actionGrabber.setText("Username and Password cannot be empty");
            actionGrabber.setFill(Color.RED);
        } else if (userGrabber.getText().isEmpty()) {
            actionGrabber.setText("Username cannot be empty");
            actionGrabber.setFill(Color.RED);
        } else if (passGrabber.getText().isEmpty()) {
            actionGrabber.setText("Password cannot be empty");
            actionGrabber.setFill(Color.RED);
        } else if (loggedInStatus == 1) {
            actionGrabber.setText("Another user is already logged in with this username");
            actionGrabber.setFill(Color.RED);
            System.out.println("A user: " + username + " has attempted access from another device (all users logged out): " + formattedDateTime);
            dbConnector.setLoggedInStatus(username, 2);
        } else { 
        	
        	if (loginSuccessful()) {
                System.out.println("A user: " + username + " has successfully logged in at: " + formattedDateTime);
                Timestamp loginTimestamp = Timestamp.valueOf(currentDateTime);
                dbConnector.setLastLoggedInTime(username, loginTimestamp);
                dbConnector.setLoggedInStatus(username, 1);
                currentFXML = "/application/fxmlScenes/Dashboard.fxml";
                CurrentFXMLInstance.initInstance(currentFXML);	//Set currentFXMLInstance to Dashboard.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
                Parent root = loader.load();
                
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("A failed login attempt has been established with user: " + username + " At: " + formattedDateTime);

                // Redirect the current user to the login.fxml scene
                if (currentUser != null && currentUser.equals(username)) {
                    Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Login.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }
    }

    @FXML
    public void switchToCreateUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/UserCreation.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void bypassUserLogin(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Dashboard.fxml"));
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}
