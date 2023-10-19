package application;
	
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.mail.SendFailedException;

/**
 * This might be redundant, need to verify
 * @author Team 5
 *
 */
public class UI {
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML private Text actionGrabber;
	@FXML private TextField userGrabber;
	@FXML private TextField passGrabber;
	@FXML private TextField tfacode  = new TextField();
	
	@FXML private TableView maintable;
	@FXML private AnchorPane anchor;
	@FXML private TextField searchRef1;
	@FXML private TextField searchRef2;
	@FXML private Text actionGrabberCreator;
	@FXML private TextField userGrabberCreator;
	@FXML private TextField passGrabberCreator;
	@FXML private TextField emailGrabberCreator;
	@FXML private Button view;
	@FXML private Button update;
	@FXML private Button insert;
	@FXML private Button clear;
	@FXML private Button back;
	@FXML private Button viewTable;
	@FXML private TableView<ObservableList> tableView = new TableView();
	
	//Toby's changes
	static byte[] encryptedPassword; // for the encryption
	static byte[] decryptedPassword; // for the decryption
	//static RSA_DSA rsa;
	static byte[] tempByteArray;
	static ObservableList<ObservableList> data;
	static TableColumn col;
	
	@FXML private Label labelStatus = new Label();
	
	@FXML private Label labelId = new Label("ID");
	@FXML private TextField textId = new TextField();
	@FXML private Label labelLastName = new Label("Last Name");
	@FXML private TextField textLastName = new TextField();
	@FXML private Label labelFirstName = new Label("First Name");
	@FXML private TextField textFirstName = new TextField();
	@FXML private Label labelMiddleName = new Label("Middle Name");
	@FXML private TextField textMiddleName = new TextField();
	@FXML private Label labelAddress = new Label("Address");
	@FXML private TextField textAddress = new TextField();
	@FXML private Label labelCity = new Label("City");
	@FXML private TextField textCity = new TextField();
	@FXML private Label labelState = new Label("State");
	@FXML private TextField textState = new TextField();
	@FXML private Label labelTelephone = new Label("Telephone");
	@FXML private TextField textTelephone = new TextField();
	@FXML private Label labelEmail = new Label("Email");
	@FXML private TextArea textEmail = new TextArea();
	@FXML private ImageView QRCode = new ImageView();
	@FXML private ComboBox<String> dd = new ComboBox();
	
	
	static ObservableList<String> ddList;
	static ObservableList<String> mmList;
	static ObservableList<String> yyList;
	
	// these objects will be used in querying the database and processing the results
	private Connection connection;
	private Statement statement;
	private ResultSet results;

	public void switchToCreateUser(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\UserCreation.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToHomepage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	@FXML public void switchToPatientInformation(ActionEvent event) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\PatientInformation.fxml"));		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
    public void initialize() throws ClassNotFoundException, SQLException { // this will load all the variables in the fields referring to components  
		ddList = FXCollections.observableArrayList("1", "2", "3");
		mmList = FXCollections.observableArrayList("Jan", "Feb", "March");
		yyList = FXCollections.observableArrayList("2020", "2021", "2023");
		dd.getItems().addAll(ddList);
		System.out.println(dd.getItems());
		
		initialDB(); // connect to the database
    }
	
	@FXML public void switchToPatientDirectory(ActionEvent event) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\PatientDirectory.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		initialDB();
		
	}
	
	public void switchToDashBoard(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\DashBoard.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public boolean loginSuccessful() {
	    String userLog = userGrabber.getText();
	    String passLog = passGrabber.getText();
	    String to = checkCredentialsInFile(userLog, passLog);
	    String from = "Verifier";
	    String host = "smtp.gmail.com";
	    int port = 587;
	    String username = "tobez103@gmail.com";
	    String password = "xx"; // the app does not use this setting, use EmailManager.java 
	    int expectedCode = (int) (Math.random() * 1000000);
	    int inputCode = 0;

	    //email properties
	    Properties properties = System.getProperties();  
	    
	    // toby's email properties
	    properties.put("mail.smtp.host", "smtp.gmail.com");
	    properties.put("mail.smtp.socketFactory.port", "587");
	    properties.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.port", "587");
	    properties.put("mail.smtp.ssl.enable", "false");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

	    // Get the email session object  
	    Session session = Session.getDefaultInstance(properties,  
	        new javax.mail.Authenticator() {  
	            protected PasswordAuthentication getPasswordAuthentication() {  
	                return new PasswordAuthentication(username, password);  
	            }  
	        });  

	    //message  
	    try {  
	        MimeMessage message = new MimeMessage(session);  
	        message.setFrom(new InternetAddress(from));  
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  
	        message.setSubject("Verification Code");  
	        message.setText("Your verification code is: " + expectedCode);  

	        //Send message to email
	        Transport transport = session.getTransport("smtp");
	        transport.connect(host, username, password);
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	        System.out.println("Verification code sent to " + to);  

	        // Display the dialog box for verification code
	        Dialog<Integer> dialog = new Dialog<>();
	        dialog.setTitle("Verification Code");
	        dialog.setHeaderText("Enter the verification code:");

	        ButtonType submitButton = new ButtonType("Submit", ButtonData.OK_DONE);
	        dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);

	        TextField verificationCodeField = new TextField();
	        Platform.runLater(() -> verificationCodeField.requestFocus());
	        dialog.getDialogPane().setContent(new VBox(8, new Label("Verification code:"), verificationCodeField));
	        dialog.setResultConverter(dialogButton -> {
	            if (dialogButton == submitButton) {
	                return Integer.parseInt(verificationCodeField.getText());
	            }
	            return null;
	        });

	        Optional<Integer> result = dialog.showAndWait();
	        if (result.isPresent()) {
	            inputCode = result.get();
	        } else {
	        	actionGrabber.setText("Email verification cancelled");
				actionGrabber.setFill(Color.RED);
				return false;
	        }

	        // Check if the input code matches the expected value
	        if (inputCode == expectedCode) {
	            System.out.println("Verification successful!");
	            return true;
	        } else if (inputCode != expectedCode) {
	        	dialog.close();
	        	actionGrabber.setText("Wrong code input. Email verification cancelled");
				actionGrabber.setFill(Color.RED);
				return false;
	        }
	    } catch (MessagingException mex) {
	        mex.printStackTrace();
	    }
	    return false;
	}
	
	@FXML protected void handleSignInAction(ActionEvent event) throws IOException {
		
		if(userGrabber.getText().equals("") & passGrabber.getText().equals("")) {
			actionGrabber.setText("Username and Password cannot be empty");
			actionGrabber.setFill(Color.RED);
		} else if(userGrabber.getText().equals("")) {
			actionGrabber.setText("Username cannot be empty");
			actionGrabber.setFill(Color.RED);
		} else if(passGrabber.getText().equals("")) {
			actionGrabber.setText("Password cannot be empty");
			actionGrabber.setFill(Color.RED);
		} 
		else {
			if(loginSuccessful() == true) {
				Parent root = FXMLLoader.load(getClass().getResource("\\fxmlScenes\\Dashboard.fxml")); // change to dashboard
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	
	
@FXML protected void handleCreateNewUsernAction(ActionEvent event) {
		
		if(userGrabberCreator.getText().equals("") & passGrabberCreator.getText().equals("") & emailGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("All fields cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(userGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Username cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(passGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Password cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} else if(emailGrabberCreator.getText().equals("")) {
			actionGrabberCreator.setText("Email cannot be empty");
			actionGrabberCreator.setFill(Color.RED);
		} 
		else {
			actionGrabberCreator.setText("User Creation Successful");
			actionGrabberCreator.setFill(Color.GREEN);
			String userCreate = userGrabberCreator.getText();
			String passCreate = passGrabberCreator.getText();
			String emailCreate = emailGrabberCreator.getText();
			saveCredentialsToFile(userCreate, passCreate, emailCreate);
		}
	}

	@FXML protected void handleAddColumn(ActionEvent event) {
		
		
		TextField htf = new TextField("");
		
		//Creates table column object with header
		TableColumn<List<String>, String> newColumn = new TableColumn<>();
		
		// Set value for table column
	    newColumn.setCellValueFactory(cellData -> {
	        List<String> row = cellData.getValue();
	        int index = maintable.getColumns().indexOf(cellData.getTableColumn());
	        if (row.size() > index) {
	            return new ReadOnlyStringWrapper(row.get(index));
	        } else {
	            return new ReadOnlyStringWrapper("");
	        }
	    });
		
	    //Without this there will be no edit text cells for a column
		newColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		//An on edit handler for table columns (gets row and column index of edited cells)
		newColumn.setOnEditCommit(action -> {
	        TablePosition<List<String>, String> position = action.getTablePosition();
	        int row = position.getRow();
	        int col = position.getColumn();
	        List<String> rowData = (List<String>) maintable.getItems().get(row);
	        rowData.set(col, action.getNewValue()); // Sets new value for edited cell data to row
	    });
		
		// For each row add a new value in table (Prevents out of bounds exception)
		ObservableList<List<String>> items = maintable.getItems();
		for (List<String> row : items) {
		    row.add("");
		}
		
		// These are for the edit text field labels at the top of each column
		newColumn.setEditable(true);
		newColumn.setResizable(true);
	    newColumn.setGraphic(htf);
	    newColumn.setMaxWidth(120);
	    newColumn.setMinWidth(80);
	    maintable.getColumns().add(newColumn);
		
	}
	
	@FXML protected void handleAddRow(ActionEvent event) {
		
		int index = maintable.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			index = maintable.getItems().size();
		} else {
			index++;
		}
		
		ObservableList<String> row = FXCollections.observableArrayList();
		for(int i = 0; i < maintable.getColumns().size(); i++) {
			row.add("");
		}
		maintable.getItems().add(index, row);
		maintable.getSelectionModel().select(index);
	}
	
	@FXML protected void handleRemoveButton(ActionEvent event) {
		maintable.getItems().removeAll(maintable.getSelectionModel().getSelectedItem());
	}
	
	@FXML protected void handleColumnRemoval(ActionEvent event) {
		ObservableList<TableColumn> columns = maintable.getColumns();
		
		if(columns.isEmpty()) {
			System.out.println("No columns present. Try adding a column"); 
		}
		
		TableColumn mostRecentColumn = columns.get(columns.size() - 1);
		
		maintable.getColumns().remove(mostRecentColumn);
	}
	
	@FXML protected void handleSearchDeletion(ActionEvent event) {
		String fIn = searchRef1.getText();
		String sIn = searchRef2.getText();
		
		for(int i = 0 ; i < maintable.getItems().size(); i++) {
			List<String> row = (List<String>) maintable.getItems().get(i);
			if(row.contains(fIn) && row.contains(sIn)) {
				maintable.getItems().remove(i);
				break;
			}
		}
	}
	
	@FXML protected void handleSave(ActionEvent event) {
		// Get the documents path
	    String desktopPath = System.getProperty("user.home") + "/Documents/";
	    
	    // Create the file name
	    String fileName = "tableview.txt";
	    
	    // Create the file
	    File file = new File(desktopPath + fileName);
	    
	    // Open the file writer
	    try (PrintWriter writer = new PrintWriter(file)) {
	        // Write the headers
	    	ObservableList<TableColumn<List<String>, ?>> columns = maintable.getColumns();
            for (TableColumn<List<String>, ?> column : columns) {
                writer.write(column.getText() + "\t");
            }
            writer.println();
	        
	        // Write the data
	        ObservableList<List<String>> rows = maintable.getItems();
	        for (List<String> row : rows) {
	            for (String cell : row) {
	                writer.print(cell);
	                writer.print("\t");
	            }
	            writer.println();
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
	}
	
	private void saveCredentialsToFile(String username, String password, String email) {
		try {
			String directory = System.getProperty("user.home");
			String filePath = directory + "/Documents/credentials.txt";
			FileWriter writer = new FileWriter(filePath, true);
			EncryptionController enc = new EncryptionController();
			String hashedPassword = enc.hashData(password);
			writer.write(username + ":" + hashedPassword + ":" + email + "\n");
			System.out.println(hashedPassword);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private String checkCredentialsInFile(String username, String password) {
	    try {
	    	EncryptionController enc = new EncryptionController();
	    	String directory = System.getProperty("user.home");
	        String filePath = directory + "/Documents/credentials.txt";
	        File file = new File(filePath);
	        Scanner scan = new Scanner(file);
	        while (scan.hasNextLine()) {
	            String data = scan.nextLine();
	            String[] part = data.split(":");
	            if (part.length == 3 && part[0].equals(username)) {
	                String storedHashedPassword = part[1];
	                String inputHashedPassword = enc.hashData(password);
	                System.out.println(inputHashedPassword);
	                System.out.println(storedHashedPassword);
	                if (storedHashedPassword.equals(inputHashedPassword)) {
	                	System.out.println(part[2]);
	                    scan.close();
	                    return part[2]; // return email address
	                } 
	            }
	        }
	        scan.close();
	        actionGrabber.setText("no match found");
			actionGrabber.setFill(Color.RED);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	@FXML public void viewTable(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
		System.out.println("view table");
		// TODO Auto-generated method stub
		initialDB();
		data = FXCollections.observableArrayList();
        try {
            //MySql query table
            String SQL = "SELECT * from `UHDD`.`test3`";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            // add table column dynamically
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // loops through the results and grabs the column name
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() { // makes the columns ready for population
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            // add to observation list
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //add to table view
            tableView.setItems(data);
            System.out.println("Data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
	}
	
	@FXML public void view(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
		System.out.println("viewed");
		String query = "SELECT * FROM `UHDD`.`test3` WHERE ID = '" + textId.getText().trim() + "'";
		
		try {
			// execute statement
			results = statement.executeQuery(query);
			loadFields(results);
			
		} catch (SQLException Ex){
			labelStatus.setText("Record failed");
			System.out.println(Ex.getMessage());
		}
	}
	
	@FXML private void update(ActionEvent event) {
		// TODO Auto-generated method stub
		String updateQuery = "UPDATE `UHDD`.`test3` SET "
				+ "FirstName = '" + textFirstName.getText().trim() + 
				"', MiddleName = '" + textMiddleName.getText().trim()+ 
				"', LastName = '" + textLastName.getText().trim() + 
				"', Address = '" + textAddress.getText().trim() + 
				"', City = '" + textCity.getText().trim() +
				"', State = '" + textState.getText().trim() + 
				"', Telephone = '" + textTelephone.getText().trim() + 
				"', Email = '" + textEmail.getText().trim() + 
				"' WHERE ID = '" + textId.getText().trim() + "';";
		
		System.out.println(updateQuery);
		
		try {
			// execute statement
			statement.executeUpdate(updateQuery);
			labelStatus.setText("Update completed or does not exist please view the ID");
			labelStatus.setTextFill(Color.GREEN);
			System.out.println("Update suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Update failed");
			labelStatus.setTextFill(Color.RED);
			System.out.println(Ex.getMessage());
		}
	}
	
	@FXML private void clear(ActionEvent event) {
		// clear the input text
		textId.setText(null);
		textLastName.setText(null);
		textFirstName.setText(null);
		textMiddleName.setText(null);
		textAddress.setText(null);
		textCity.setText(null);
		textState.setText(null);
		textTelephone.setText(null);
		textEmail.setText(null);
	}
	
	@FXML private void insert(ActionEvent event) {
		
		String insertQuery = "INSERT INTO `UHDD`.`test3` "
				+ "(ID, FirstName, MiddleName, LastName, Address, City, State, Telephone, Email) "
				+ "VALUES ('" + textId.getText().trim() + "', '" + textFirstName.getText().trim() + 
				"', '" + textMiddleName.getText().trim()+ "', '" + textLastName.getText().trim() + 
				"', '" + textAddress.getText().trim() + "', '" + textCity.getText().trim() +
				"', '" + textState.getText().trim() + "', '" + textTelephone.getText().trim() + 
				"', '" + textEmail.getText().trim() + "');";
		
		//System.out.println(insertQuery);
		
		try {
			// execute statement
			statement.executeUpdate(insertQuery);
			labelStatus.setText("Insert completed");
			labelStatus.setTextFill(Color.GREEN);
			System.out.println("Insert suceeded");
			
		} catch (SQLException Ex){
			labelStatus.setText("Insert failed");
			labelStatus.setTextFill(Color.RED);
			System.out.println(Ex.getMessage());
		}
		
	}

  
  	//Overview button actions
    @FXML
    private void fetchDiagnoses() {
        System.out.println("Hopefully gets 'diagnoses' info");
    }
    
    // Other controller methods and variables
	@FXML
	private void fetchHistory() {
	    System.out.println("Get patient history");
	}
	
	// Other controller methods and variables
	@FXML
	private void fetchPerscription() {
	    System.out.println("Get Patient perscription");
	}
	
	// Other controller methods and variables
	@FXML
	private void fetchReport() {
	    System.out.println("Gets report");
	}
	
	// Other controller methods and variables
	private void loadFields(ResultSet results) throws SQLException {
		
		if (results.next()) {
			textLastName.setText(results.getString(2));
			textFirstName.setText(results.getString(3));
			textMiddleName.setText(results.getString(4));
			textAddress.setText(results.getString(5));
			textCity.setText(results.getString(6));
			textState.setText(results.getString(7));
			textTelephone.setText(results.getString(8));
			textEmail.setText(results.getString(9));
			labelStatus.setText("Record found");
			labelStatus.setTextFill(Color.GREEN);
		} else {
			textLastName.setText("");
			textFirstName.setText("");
			textMiddleName.setText("");
			textAddress.setText("");
			textCity.setText("");
			textState.setText("");
			textTelephone.setText("");
			textEmail.setText("");
			labelStatus.setText("Record not found");
			labelStatus.setTextFill(Color.RED);
		}
	}
	
	public void initialDB() throws ClassNotFoundException, SQLException {
		
		// loads and checks the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("Driver loaded:");
		
		// connection for database...make sure the URL is correct JDBC:MYSQL
		String url = "jdbc:mysql://127.0.0.1:3306/UHDD";
		String username = "root";
		String password = "1234";
		
		// connect to the database
		try {
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected:");
			labelStatus.setText("Database connected");
			labelStatus.setTextFill(Color.GREEN);
			statement = connection.createStatement();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			labelStatus.setText("Connection failed");
			labelStatus.setTextFill(Color.RED);
		} 
	}
	

}

