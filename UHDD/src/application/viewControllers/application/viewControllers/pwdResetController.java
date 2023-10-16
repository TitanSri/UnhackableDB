package application.viewControllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import application.CredentialManager;
import application.LoginResult;
import application.UserSession;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is used to control the password reset FXML/UI
 * @author User
 *
 */
public class pwdResetController {
    @FXML
    private Button btnResetPwd;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtOldPwd;
    @FXML
    private TextField txtNewPwd;
    @FXML
    private TextField txtVerifyPwd;
    @FXML
    private Text txtStatus;
    
    public void resetPwd() throws Exception {
        String oldPwd = txtOldPwd.getText();
        String newPwd = txtNewPwd.getText();
        String verifyPwd = txtVerifyPwd.getText();
        String username = UserSession.getInstance().getUserName();
        System.out.println(username + " is the username");
        CredentialManager credentialManager = new CredentialManager();
    

        if (oldPwd.equals("") || newPwd.equals("") || verifyPwd.equals("")) {
            txtStatus.setText("Please fill in all fields");
        } else if (!newPwd.equals(verifyPwd)) {
            txtStatus.setText("New passwords do not match");
        } else if (newPwd.equals(oldPwd)) {
            txtStatus.setText("New password cannot be the same as old password");
        } else if (newPwd.length() < 8 || newPwd.length() > 64) {
            txtStatus.setText("New password must be between 8 and 64 characters");
        } else if (credentialManager.verifyPassword(username, oldPwd)){
            LoginResult result = credentialManager.verifyMFA(username);
            if (result == LoginResult.SUCCESSFUL) {
                // Handle successful login
                credentialManager.changePasswordInDB(username, newPwd);
                txtStatus.setText("Password Updated");
                txtStatus.setStroke(javafx.scene.paint.Color.GREEN);
                PauseTransition delay = new PauseTransition(Duration.seconds(2)); // Creates a 2 seconds pause
                delay.setOnFinished( event -> {
                    // Closes the window after the pause
                    Stage stage = (Stage) txtStatus.getScene().getWindow(); 
                    stage.close();
                });
                delay.play();               
            } else if (result == LoginResult.WRONG_CODE) {
                txtStatus.setText("Wrong code input. MFA verification cancelled");
                txtStatus.setFill(Color.RED);        
            } else if (result == LoginResult.CANCELLED) {
                // Handle login cancelled scenario
                txtStatus.setText("MFA verification cancelled");
                txtStatus.setFill(Color.RED);  
            } else {
                // Handle any other result if necessary
               
            }     
        }
        else {
            System.out.print(UserSession.getInstance().getUserName());
            if(credentialManager.verifyPassword(username, oldPwd)){
                System.out.println("Password is correct");
            }
            txtStatus.setText("Old password is incorrect");
        }
    }

    @FXML
    protected void handlePwdResetAction(ActionEvent event) throws Exception{
        resetPwd();
    }
}

