package application;


import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * This is used in the app starter
 * @author User
 *
 */
public class Main extends Application {
	
	/**
	 * This is used in the calendar app, calendar controller, patient directory, user creation
	 * dashboard and patient info view 
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/Login.fxml"));
			Scene scene = new Scene(root,915,500);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * This is only used in this class
	 */
	@Override
	 public void stop() throws Exception{
		UserSession us = new UserSession();
		DBConnector db = new DBConnector();
		db.initialiseDB();
		String name = us.getUserName();
		System.out.println("Stage is closing: " + name);
		db.setLoggedInStatus(name, 0);
		db.closeConnection();
	 } 
	
}
