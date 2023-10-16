package application.viewControllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import application.CalendarApp;
import application.CreateEncryptedPdf;
import application.DBConnector;
import application.Patient;
import application.PatientService;
import application.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import application.UserSession;

/**
 * This is used to control the patient info view FXML/UI
 * @author User
 *
 */
public class PatientInfoViewController {
	private static Stage calendarStage;
	private static Parent calendarRoot;
	private static CalendarApp myCalendar;
	private Stage stage;
	private Scene scene;
	@FXML
	private Pane patientDirectoryIVPane;
	@FXML
	private Pane appointmentsDBPane;
	@FXML
	private Pane patientNotesDBPane;
	@FXML
	private Pane dashboardIVPane;
	
	//labels that update
	@FXML private Text fullnameTXT;
	@FXML private Text genderTXT;
	@FXML private Text dobTXT;
	@FXML private Text locationTXT;
	@FXML private Text patientIdTXT;
	@FXML private Text phoneNoTXT;
	@FXML private Text emailTXT;
	@FXML private Text addressTXT;
	@FXML private Text insuranceNumberTXT;
	@FXML private Text detailsTXT;
	@FXML private Text cityTXT;
	@FXML private Text emergencyNoTXT;
	@FXML private Button nextPatientBTN;
	@FXML private Button btnPDFCreate;
	@FXML private Text viewPatientInfoBtn;
	@FXML private Text viewPatientInfoBtn2;
	@FXML private Text viewPatientInfoBtn3;
	@FXML private Text viewPatientInfoBtn4;
	@FXML private Text familyMedicalHistoryTXT;
	@FXML private Text progressNotesTXT;

	//Prescribed Medication Pane 
	@FXML private Text medName1;
	@FXML private Text medName2;
	@FXML private Text medName3;
	@FXML private Text medExpiry1;
	@FXML private Text medExpiry2;
	@FXML private Text medExpiry3;
	

	//Diagnosis Pane
	@FXML private Text diagnosisTxt1;
	@FXML private Text diagnosisTxt2;
	@FXML private Text diagnosisTxt3;
	@FXML private Text diagnosisDateTxt1;
	@FXML private Text diagnosisDateTxt2;
	@FXML private Text diagnosisDateTxt3;
	@FXML private Text diagnosisIdTxt1;
	@FXML private Text diagnosisIdTxt2;
	@FXML private Text diagnosisIdTxt3;

	//Patient Notes
	@FXML private Text txtNoteDate1;
	@FXML private Text txtNote1;
	@FXML private Text txtNoteDate2;
	@FXML private Text txtNote2;
	@FXML private Text txtNoteDate3;
	@FXML private Text txtNote3;
	@FXML private Text txtScriptInc1;
	@FXML private Text txtScriptInc2;
	@FXML private Text txtScriptInc3;

	//Patient Notes PopUp
	@FXML private Text txtEnterNoteAsId;
	@FXML private Text txtNotePatientName;
	@FXML private TextArea txtAreaNote;
	@FXML private RadioButton radBtnYes;
	@FXML private RadioButton radBtnNo;
	@FXML private TextField inMedicationName;
	@FXML private TextField inScriptValidDays;
	@FXML private Button btnNoteSaveNClose;
	@FXML private Text txtCharCountWarn;
	private static final int MAX_LENGTH = 500;
	@FXML private Text txtNonIntWarn;
	@FXML private TextField inDiagnosisName;
	@FXML private ChoiceBox<String> inDiagnosisSev;
	@FXML private RadioButton radBtnDiagYes;
	@FXML private RadioButton radBtnDiagNo;
	@FXML private RadioButton radBtnDiagIdYes;
	@FXML private RadioButton radBtnDiagIdNo;
	@FXML private TextField inDiagnosisId;
	@FXML private Text txtInvalidDiagId;

	//Patient Progress Plan
	@FXML private Text txtPPDiagnosisName;
	@FXML private TextArea txtAreaInitialDetails;
	@FXML private TextArea txtAreaGoalStatus;
	@FXML private TextArea txtAreaRecentUpdate;
	@FXML private Text txtMedicationToRectify;
	@FXML private Text txtPPExpectedTimeline;

	//Patient Progress Plan PopUp
	@FXML private Text txtEnterPPAsId;
	@FXML private TextField inPPDiagnosisId;
	@FXML private TextArea txtAreaPPInitialDetails;
	@FXML private TextArea txtAreaPPGoal;
	@FXML private DatePicker datePickPPRemediationDate;
	@FXML private Button btnPPSaveNClose;
	@FXML private Text txtPPInvalidDiagId;
	@FXML private Text txtIntPlanDetailsWarn;
	@FXML private Text txtPlanGoalWarn;
	@FXML private Text txtInvalidRemediationDate;


	String currentFXML;
	static Integer patient;
	
	DBConnector dbConnector = new DBConnector();
	
	public void setTextFieldsToPatientId(String Id) throws Exception {
		dbConnector.initialiseDB();
		ResultSet patientDetails = dbConnector.QueryReturnResultsFromPatientId(Id);
		ResultSetMetaData meta = patientDetails.getMetaData(); // not need at the moment, this will get the mata data such as column size
		
		if(patientDetails.next()) {
			fullnameTXT.setText(patientDetails.getString("FirstName") + " " + patientDetails.getString("MiddleName") + " " + patientDetails.getString("LastName"));		
			dobTXT.setText(patientDetails.getString("DateOfBirth"));
			locationTXT.setText(patientDetails.getString("City"));
			patientIdTXT.setText(patientDetails.getString("ID"));
			insuranceNumberTXT.setText(patientDetails.getString("HealthInsuranceNumber"));
			detailsTXT.setText(patientDetails.getString("Details"));	
			familyMedicalHistoryTXT.setText(patientDetails.getString("FamilyMedicalHistory"));
			progressNotesTXT.setText(patientDetails.getString("ProgressNotes"));
	
			
		} 
		
		System.out.println(patient);
		
		dbConnector.closeConnection();
	}

	public void setPatientOverviewTxtFields (Patient patient){
		fullnameTXT.setText(patient.getGivenName() + " " + patient.getMiddleName() + " " + patient.getFamilyName());
		genderTXT.setText(patient.getGender());
		dobTXT.setText(patient.getDateOfBirth());
		locationTXT.setText(patient.getCity());
		patientIdTXT.setText(String.valueOf(patient.getId()));
		insuranceNumberTXT.setText(patient.getHealthInsuranceNumber());
		//currentFXML = CurrentFXMLInstance.getInstance().getCurrentFXML();
		System.out.println(currentFXML + " on set patient overview");
		if(currentFXML.equals("/application/fxmlScenes/PatientInfoViewOverview.fxml")){
			phoneNoTXT.setText(patient.getTelephone());
			emailTXT.setText(patient.getEmail());
			addressTXT.setText(patient.getAddress());
			cityTXT.setText(patient.getCity());
			emergencyNoTXT.setText(patient.getEmergencyContactNumber());
		}
	}

	public void setDiagnosisOverviewTxtFields (Patient patient) throws Exception {
		dbConnector.initialiseDB();
		ResultSet diagnosisResultSet = dbConnector.QueryReturnResultsDiagnosisFromPatientId(String.valueOf(patient.getId()));
		int count = 0;
		while(diagnosisResultSet.next() && count < 3){
			String diagnosisName = diagnosisResultSet.getString("diagnosisName");
			String diagnosisId = diagnosisResultSet.getString("diagnosisId");
			LocalDate diagnosisDate = null;
			if (diagnosisResultSet.getDate("diagnosedDate") != null) {
				diagnosisDate = diagnosisResultSet.getDate("diagnosedDate").toLocalDate();
				switch(count) {
				case 0:
					diagnosisTxt1.setText(diagnosisName);
					diagnosisDateTxt1.setText(diagnosisDate.toString());
					diagnosisIdTxt1.setText("Id: " + diagnosisId);
					break;
				case 1:
					diagnosisTxt2.setText(diagnosisName);
					diagnosisDateTxt2.setText(diagnosisDate.toString());
					diagnosisIdTxt2.setText("Id: " + diagnosisId);
					break;
				case 2:
					diagnosisTxt3.setText(diagnosisName);
					diagnosisDateTxt3.setText(diagnosisDate.toString());
					diagnosisIdTxt3.setText("Id: " + diagnosisId);
					break;
			}
			count++;
			} 
					
		}
		switch(count){
			case 0:
				diagnosisTxt1.setText("This patient currently has no diagnoses.");
				diagnosisTxt1.setFont(Font.font("System", FontWeight.BOLD, 12));
				diagnosisTxt1.setFill(Color.web("#9a9797"));
				diagnosisDateTxt1.setText("");
				diagnosisIdTxt1.setText("");
				diagnosisTxt2.setText("");
				diagnosisDateTxt2.setText("");
				diagnosisIdTxt2.setText("");
				diagnosisTxt3.setText("");
				diagnosisDateTxt3.setText("");
				diagnosisIdTxt3.setText("");
				break;
			case 1:
				diagnosisTxt2.setText("");
				diagnosisDateTxt2.setText("");
				diagnosisIdTxt2.setText("");
				diagnosisTxt3.setText("");
				diagnosisDateTxt3.setText("");
				diagnosisIdTxt3.setText("");
				break;
			case 2:
				diagnosisTxt3.setText("");
				diagnosisDateTxt3.setText("");
				diagnosisIdTxt3.setText("");
				break;
		}
	}

	public void setMedicationOverviewTxtFields (Patient patient) throws Exception{
		dbConnector.initialiseDB();
		ResultSet medicationResultSet = dbConnector.QueryReturnResultsMedicationFromPatientId(String.valueOf(patient.getId()));
		int count = 0;
		while(medicationResultSet.next() && count < 3){
			String medicationName = medicationResultSet.getString("medication_name");
			LocalDate expiredDate = null;
			if (medicationResultSet.getDate("expired_date") != null) {
				expiredDate = medicationResultSet.getDate("expired_date").toLocalDate();
				switch(count) {
				case 0:
					medName1.setText(medicationName);
					medExpiry1.setText(expiredDate.toString());
					break;
				case 1:
					medName2.setText(medicationName);
					medExpiry2.setText(expiredDate.toString());
					break;
				case 2:
					medName3.setText(medicationName);
					medExpiry3.setText(expiredDate.toString());
					break;
			}
			count++;
			}
					
		}
		switch(count) {
				case 0:
					medName1.setText("No medication currently prescribed for this patient");
					medName1.setFont(Font.font("System", FontWeight.BOLD, 12));
					medName1.setFill(Color.web("#9a9797"));
					medExpiry1.setText("");
					medName2.setText("");
					medExpiry2.setText("");
					medName3.setText("");
					medExpiry3.setText("");
					break;
				case 1:
					medName2.setText("");
					medExpiry2.setText("");
					medName3.setText("");
					medExpiry3.setText("");
					break;
				case 2:
					medName3.setText("");
					medExpiry3.setText("");
					break;
		}
		dbConnector.closeConnection();
	}

	public void setNoteSummaryDetails(Patient patient) throws Exception{
		dbConnector.initialiseDB();
		ResultSet noteResultSet = dbConnector.QueryReturnResultsNotesFromPatientId(String.valueOf(patient.getId()));
		
		int count = 0;
		while(noteResultSet.next() && count < 3){
			String noteSummary = noteResultSet.getString("noteText");
			LocalDate noteDate = null;
			if (noteResultSet.getDate("noteEnteredDate") != null) {
				noteDate = noteResultSet.getDate("noteEnteredDate").toLocalDate();
				int scriptInc = noteResultSet.getInt("scriptIncluded");
				if(noteSummary.length() >= 200){
				String trimmedNoteSummary = noteSummary.substring(0, 200) + "...";
				noteSummary = trimmedNoteSummary;
				}
				String scriptTxString = "No";
				if(scriptInc == 1){
					scriptTxString = "Yes";
				}
				switch(count) {
					case 0:
						txtNote1.setText(noteSummary);
						txtNoteDate1.setText(noteDate.toString());
						txtScriptInc1.setText(scriptTxString);
						break;
					case 1:
						txtNote2.setText(noteSummary);
						txtNoteDate2.setText(noteDate.toString());
						txtScriptInc2.setText(scriptTxString);
						break;	
					case 2:
						txtNote3.setText(noteSummary);
						txtNoteDate3.setText(noteDate.toString());
						txtScriptInc3.setText(scriptTxString);
						break;
					
				}
				count++;
			}
					
		}
		switch(count) {
				case 0:
					txtNote1.setText("No notes have been added to this patient's record.");
					txtNote1.setFont(Font.font("System", FontWeight.BOLD, 12));
					txtNote1.setFill(Color.web("#9a9797"));
					txtNoteDate1.setText("");
					txtScriptInc1.setText("");
					txtNoteDate2.setText("");
					txtScriptInc2.setText("");
					txtNote2.setText("");
					txtNoteDate3.setText("");
					txtScriptInc3.setText("");
					txtNote3.setText("");
					break;
				case 1:
					txtNoteDate2.setText("");
					txtScriptInc2.setText("");
					txtNote2.setText("");
					txtNoteDate3.setText("");
					txtScriptInc3.setText("");
					txtNote3.setText("");
					break;
				case 2:
					txtNoteDate3.setText("");
					txtScriptInc3.setText("");
					txtNote3.setText("");
					break;
		}
		dbConnector.closeConnection();
	}

	@FXML
	public void saveAndAddNote() throws Exception{
		dbConnector.initialiseDB();
		Patient patient = PatientService.getInstance().getCurrentPatient();
		String noteText = txtAreaNote.getText();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime = now.format(formatter);
		int scriptIncluded = 0;
		String formatFutureDate = null;
		LocalDateTime futureDate = null;
		String MedicationName = null;
		int parsedDiagnosisId = 0;
		Boolean diagnosisIdValid = true;
		Boolean validExpiryDate = true;
		Boolean textAreaValid = false;

		


		if(radBtnYes.isSelected()){
			validExpiryDate = false;
			MedicationName = inMedicationName.getText();
			String input = inScriptValidDays.getText();
			try {
				int value = Integer.parseInt(input);
				if (value < 1 || value > 730) {
					// Value is not between 1 and 730, handle this case
					txtNonIntWarn.setText(input + " is not between 1 and 730");
					txtNonIntWarn.setVisible(true);
				} else {
					// Value is valid
					futureDate = now.plusDays(value);
					formatFutureDate = futureDate.format(formatter);
					validExpiryDate = true;
					scriptIncluded = 1;
				}
			} catch (NumberFormatException e) {
				// Input is not an integer, handle this case
				txtNonIntWarn.setText(input + " is not an integer");
				txtNonIntWarn.setVisible(true);
			}
		}
		if(radBtnDiagIdYes.isSelected()){
			diagnosisIdValid = false;
			String diagnosisId = inDiagnosisId.getText();
			try {
				parsedDiagnosisId = Integer.parseInt(diagnosisId);
				if(dbConnector.verifyDiagnosisIdExists(String.valueOf(parsedDiagnosisId))){
					diagnosisIdValid = true;
				} else {
					txtInvalidDiagId.setText(diagnosisId + " Does not exist");
				}
			} catch (NumberFormatException e) {
				txtInvalidDiagId.setText(diagnosisId + " is not a number");
			}
		}

		if(noteText.length() > 0){
			textAreaValid = true;
		} else {
			txtCharCountWarn.setVisible(true);
			txtCharCountWarn.setText("Please enter a note");
		}

		if(validExpiryDate && diagnosisIdValid && textAreaValid){
			dbConnector.createNewNoteExecuteQuery(String.valueOf(patient.getId()),UserSession.getInstance().getId(), noteText, formatDateTime, String.valueOf(scriptIncluded));

			ResultSet notes = dbConnector.QueryNoteIdForDiagnosis(String.valueOf(patient.getId()),UserSession.getInstance().getId(), noteText, formatDateTime, String.valueOf(scriptIncluded));
			notes.next();
			String noteId = notes.getString("noteId");
			if(radBtnDiagIdYes.isSelected()){
			dbConnector.CreateNewNoteDiagnosisIdLink(noteId, String.valueOf(parsedDiagnosisId));
			}
			if(scriptIncluded == 1){
				dbConnector.createNewMedicationExecuteQuery(String.valueOf(patient.getId()), MedicationName, formatDateTime, formatFutureDate, noteId, UserSession.getInstance().getId());
			}
			if(radBtnDiagYes.isSelected()){
				String diagnosisName = inDiagnosisName.getText();
				String diagnosisSev = inDiagnosisSev.getValue();		
				dbConnector.createNewDiagnosisExecuteQuery(String.valueOf(patient.getId()), diagnosisName, diagnosisSev, formatDateTime, UserSession.getInstance().getId());
			}
			dbConnector.closeConnection();
			Stage stage = (Stage) btnNoteSaveNClose.getScene().getWindow();
			stage.close();
		} 
		
		
	}

	public void setProgressPlan(String patientId) throws Exception{
		System.out.println("do we begin to set Progress plan?");
		String strMedicationNames = " ";
		String diagId = "0";
		dbConnector.initialiseDB();

		//get the most recent diagnosisId for the patients progress plan
		ResultSet getDiagIdResultSet = dbConnector.QueryReturnResultsMostRecentDiagIdForPP(patientId);
		System.out.println(patientId + " is the patient id");
		if(getDiagIdResultSet.next()){
			System.out.println("do we get into the if statement?");
			diagId = getDiagIdResultSet.getString("diagnosisId");
		}
		System.out.println(diagId + " is the diagnosis id");
		ResultSet progressplanResultSet = dbConnector.QueryReturnResultsDiagIdForProgressPlan(diagId);

		// Check if the ResultSet has any rows
		if (progressplanResultSet.next()) {
			System.out.println("do we get into the if statement?");
			txtPPDiagnosisName.setText(progressplanResultSet.getString("diagnosisName"));
			txtAreaInitialDetails.setText(progressplanResultSet.getString("initialDetails"));
			txtAreaGoalStatus.setText(progressplanResultSet.getString("progressPlanGoal"));
			txtAreaRecentUpdate.setText(progressplanResultSet.getString("noteText"));

			ResultSet medicationNames = dbConnector.QueryReturnResultsDiagIdForMedicationNames(diagId);
			while (medicationNames.next()) {
				strMedicationNames += medicationNames.getString("medication_name");
				strMedicationNames += ", ";
			}

			if (strMedicationNames.endsWith(", ")) {
				strMedicationNames = strMedicationNames.substring(0, strMedicationNames.length() - 2);
				txtMedicationToRectify.setText(strMedicationNames);
			} else {
				txtMedicationToRectify.setText("No Medication to rectify");
			}
		} else {
			txtPPDiagnosisName.setText("No Progress Plan");
			txtMedicationToRectify.setText("No Medication to rectify");
		}

		dbConnector.closeConnection();

	}

	@FXML
	public void createProgressPlan() throws Exception{
		
		String diagnosisId = inPPDiagnosisId.getText();
		String initialDetails = txtAreaPPInitialDetails.getText();
		String progressPlanGoal = txtAreaPPGoal.getText();
		LocalDate currentDate = LocalDate.now();
		LocalDate expectedRemediationDate = datePickPPRemediationDate.getValue();
		Boolean diagnosisIdValid = false;
		Boolean initialDetailsValid = false;
		Boolean planGoalValid = false;
		Boolean remediationDateValid = false;
		txtPlanGoalWarn.setText("");
		txtPPInvalidDiagId.setText("");
		txtIntPlanDetailsWarn.setText("");
		txtInvalidRemediationDate.setText("");

		dbConnector.initialiseDB();
		try {
			int parsedDiagnosisId = Integer.parseInt(diagnosisId);
			if(dbConnector.verifyDiagnosisIdExists(String.valueOf(parsedDiagnosisId))){
				diagnosisIdValid = true;
			} else {
				txtPPInvalidDiagId.setText(diagnosisId + " Does not exist");
			}
		} catch (NumberFormatException e) {
			txtPPInvalidDiagId.setText(diagnosisId + " is not a number");
		}

		if(initialDetails.length() > 0 && initialDetails.length() < 201){
			initialDetailsValid = true;
		} else {
			txtIntPlanDetailsWarn.setVisible(true);
			txtIntPlanDetailsWarn.setText("Please enter a note between 1 and 200 characters");
		} 

		if(progressPlanGoal.length() > 0 && progressPlanGoal.length() < 201){
			planGoalValid = true;
		} else {
			txtPlanGoalWarn.setVisible(true);
			txtPlanGoalWarn.setText("Please enter a note between 1 and 200 characters");
		}
		if (expectedRemediationDate.isAfter(currentDate.plusDays(1)) || expectedRemediationDate.equals(currentDate.plusDays(1))) {
			remediationDateValid = true;
		} else {
			txtInvalidRemediationDate.setText("Invalid expected remediation date. It should be at least 1 day ahead of the current date.");
		}
		if(diagnosisIdValid && initialDetailsValid && planGoalValid && remediationDateValid){
			dbConnector.CreateNewProgressPlanExecuteQuery(diagnosisId, initialDetails, progressPlanGoal, expectedRemediationDate.toString());
			dbConnector.closeConnection();
			Stage stage = (Stage) btnPPSaveNClose.getScene().getWindow();
			stage.close();
		} else {
			dbConnector.closeConnection();
		}
		
	}

	//INITIALISE METHOD
	@FXML
	public void initialize() throws Exception {
		Patient patientNew = PatientService.getInstance().getCurrentPatient();
		currentFXML = CurrentFXMLInstance.getInstance().getCurrentFXML();
		System.out.println("Current FXML: " + currentFXML);
		if(currentFXML.equals("/application/fxmlScenes/PopUpAddPatientNote.fxml")){
			inDiagnosisSev.getItems().addAll("Mild", "Moderate", "Severe");
			inDiagnosisSev.setValue("Mild");
			txtEnterNoteAsId.setText("Entering note as: " + UserSession.getInstance().getUserName());
			txtNotePatientName.setText("Patient Name: " + patientNew.getGivenName() + " " + patientNew.getFamilyName());
			txtAreaNote.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null && newValue.length() > MAX_LENGTH) {
					txtAreaNote.setText(oldValue);
					txtCharCountWarn.setVisible(true);
				} else {
					txtCharCountWarn.setVisible(false);
				}
			});
		} else if (currentFXML.equals("/application/fxmlScenes/PopUpAddProgressPlan.fxml")){
			
		}
		else {setPatientOverviewTxtFields(patientNew);}
		
		if(currentFXML.equals("/application/fxmlScenes/PatientInfoViewOverview.fxml")){
			setMedicationOverviewTxtFields(patientNew);
			setDiagnosisOverviewTxtFields(patientNew);
		} else if (currentFXML.equals("/application/fxmlScenes/PatientInfoViewPatientNotes.fxml")){
			setNoteSummaryDetails(patientNew);
		} else if (currentFXML.equals("/application/fxmlScenes/PatientInfoViewProgressPlan.fxml")){
			System.out.println("Are we getting here?");
			setProgressPlan(String.valueOf(patientNew.getId()));
		}	
	}
	//INITIALISE ^^^^^

	@FXML
	public void addPatientNote(MouseEvent mouseEvent) throws IOException{
			currentFXML = "/application/fxmlScenes/PopUpAddPatientNote.fxml";
			CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
			Stage popupStage = new Stage();
            Parent popupRoot = FXMLLoader.load(getClass().getResource(currentFXML));
            Scene popupScene = new Scene(popupRoot);
            popupStage.setScene(popupScene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
	}

	@FXML
	public void addPatientProgressPlan(MouseEvent mouseEvent) throws IOException{
			currentFXML = "/application/fxmlScenes/PopUpAddProgressPlan.fxml";
			CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
			Stage popupStage = new Stage();
            Parent popupRoot = FXMLLoader.load(getClass().getResource(currentFXML));
            Scene popupScene = new Scene(popupRoot);
            popupStage.setScene(popupScene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
	}

	@FXML
	public void nextPatient() throws Exception {
		setTextFieldsToPatientId(patient.toString());
	}
	
	@FXML 	
	public void switchToPatientInformation(MouseEvent mouseEvent) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmlScenes/PatientInformation.fxml"));		
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 	
	public void switchToDasboard(MouseEvent mouseEvent) throws Exception {
		currentFXML = "/application/fxmlScenes/Dashboard.fxml";	
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();
	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	
	@FXML	
	public void switchToPatientDirectory(MouseEvent mouseEvent) throws IOException {
		currentFXML = "/application/fxmlScenes/PatientDirectory.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();
	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	
	public void switchToCalendar(MouseEvent mouseEvent) throws Exception {
		 if (myCalendar == null) {
		 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/fxmlScenes/Calendar.fxml"));
		 calendarRoot = (Parent) fxmlLoader.load();
		 calendarStage = new Stage();
		 calendarStage.setScene(new Scene(calendarRoot));
		 calendarStage.show();
		 
		myCalendar = new CalendarApp();
		 myCalendar.start(calendarStage);
		 } else calendarStage.show();
	}
	
	@FXML	
	public void switchToPatientInfoView(MouseEvent mouseEvent) throws IOException {
	    currentFXML = "/application/fxmlScenes/PatientInfoViewOverview.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	    System.out.println(currentFXML);
	}
	
	@FXML	
	public void switchToPatientInfoViewProgressPlan(MouseEvent mouseEvent) throws IOException {
		currentFXML = "/application/fxmlScenes/PatientInfoViewProgressPlan.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML); 
		FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
		Parent root = loader.load();
		stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		System.out.println(currentFXML);
	}
	
	@FXML	
	public void switchToPatientInfoViewPatientNotes(MouseEvent mouseEvent) throws IOException {
	    currentFXML = "/application/fxmlScenes/PatientInfoViewPatientNotes.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    Map<String, Object> namespace = loader.getNamespace();

	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	    System.out.println(currentFXML);
	}
	
	@FXML	
	public void switchToPatientInfoViewDocuments(MouseEvent mouseEvent) throws IOException {
	    currentFXML = "/application/fxmlScenes/PatientInfoViewDocuments.fxml";
		CurrentFXMLInstance.getInstance().setCurrentFXML(currentFXML);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(currentFXML));
	    Parent root = loader.load();
	    stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	    System.out.println(currentFXML);
	}
	
	@FXML 
	public void createPatientPDF(MouseEvent mouseEvent) throws Exception {
		String os = System.getProperty("os.name").toLowerCase();
		String filePath;

		if (os.contains("win")) {
			// Windows
			String userProfile = System.getenv("USERPROFILE");
			filePath = userProfile + "\\Documents\\patientDetails.pdf";
		} else if (os.contains("mac")) {
			// macOS
			filePath = System.getProperty("user.home") + "/Documents/patientDetails.pdf";
		} else {
			// Linux - no idea if this works or not, can't test
			filePath = System.getProperty("user.home") + "/Documents/patientDetails.pdf";
		}
		Patient patientNew = PatientService.getInstance().getCurrentPatient();
		String patientId = String.valueOf(patientNew.getId());
		String ownerPassword = "owner";
		String userPassword = "user";
		String patientName = patientNew.getGivenName() + " " + patientNew.getFamilyName();
		CreateEncryptedPdf.createPatientDetailsPdf(patientId, filePath, ownerPassword, userPassword, patientName);
	}

	@FXML 
	public void createMedicationPdf(MouseEvent mouseEvent) throws Exception {
		String os = System.getProperty("os.name").toLowerCase();
		String filePath;

		if (os.contains("win")) {
			// Windows
			String userProfile = System.getenv("USERPROFILE");
			filePath = userProfile + "\\Documents\\patientDetails.pdf";
		} else if (os.contains("mac")) {
			// macOS
			filePath = System.getProperty("user.home") + "/Documents/patientDetails.pdf";
		} else {
			// Linux - no idea if this works or not, can't test
			filePath = System.getProperty("user.home") + "/Documents/patientDetails.pdf";
		}
		Patient patientNew = PatientService.getInstance().getCurrentPatient();
		String patientId = String.valueOf(patientNew.getId());
		String ownerPassword = "owner";
		String userPassword = "user";
		String patientName = patientNew.getGivenName() + " " + patientNew.getFamilyName();
		CreateEncryptedPdf.createMedicationPdf(patientId, filePath, ownerPassword, userPassword, patientName);
	}

	@FXML
	public void switchToOverviewTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible 
		viewPatientInfoBtn.setFont(Font.font("System", FontWeight.BOLD, 14));
		viewPatientInfoBtn2.setFont(Font.font("System",  14));
		viewPatientInfoBtn3.setFont(Font.font("System", 14));
		viewPatientInfoBtn4.setFont(Font.font("System", 14));
		familyMedicalHistoryTXT.setVisible(false);
		progressNotesTXT.setVisible(false);
		// overview tab has no text to view as of code written 
	}
	
	@FXML
	public void switchToPatientProgressAndPlanTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible 
		viewPatientInfoBtn.setFont(Font.font("System", 14));
		viewPatientInfoBtn2.setFont(Font.font("System", FontWeight.BOLD, 14));
		viewPatientInfoBtn3.setFont(Font.font("System", 14));
		viewPatientInfoBtn4.setFont(Font.font("System", 14));
		familyMedicalHistoryTXT.setVisible(true);
		progressNotesTXT.setVisible(false);;
	}
	
	@FXML
	public void switchToPatientNotesTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible
		viewPatientInfoBtn.setFont(Font.font("System", 14));
		viewPatientInfoBtn2.setFont(Font.font("System", 14));
		viewPatientInfoBtn3.setFont(Font.font("System",FontWeight.BOLD , 14));
		viewPatientInfoBtn4.setFont(Font.font("System", 14));
		familyMedicalHistoryTXT.setVisible(false);
		progressNotesTXT.setVisible(true);;
	}
	
	@FXML
	public void switchToPatientDocumentsTab(MouseEvent mouseEvent) throws IOException {
		// this will change the subheader text to bold and make the text visible 
		viewPatientInfoBtn.setFont(Font.font("System", 14));
		viewPatientInfoBtn2.setFont(Font.font("System", 14));
		viewPatientInfoBtn3.setFont(Font.font("System", 14));
		viewPatientInfoBtn4.setFont(Font.font("System",FontWeight.BOLD , 14));
		familyMedicalHistoryTXT.setVisible(false);
		progressNotesTXT.setVisible(false);;
		// document tab has no text to view as of code written 
	}

	@FXML
	public void setTxtFieldsNoEdit (MouseEvent mouseEvent) throws IOException {

		if(radBtnNo.isSelected()){
			inMedicationName.setEditable(false);
			inScriptValidDays.setEditable(false);
			radBtnYes.setSelected(false);
		}
	}

	@FXML
	public void setTxtFieldsEdit (MouseEvent mouseEvent) throws IOException {
		String role = UserSession.getInstance().getRole();
		if(radBtnYes.isSelected()){
			if ("Nurse".equals(role)) {
				Alert alert = new Alert(Alert.AlertType.WARNING, "Sorry, only users with role type doctor can add prescribe medication", ButtonType.OK);
				alert.setTitle("Role Check");
				alert.setHeaderText(null);
				alert.showAndWait();
				radBtnYes.setSelected(false);
				radBtnNo.setSelected(true);
			} else {
				inMedicationName.setEditable(true);
				inScriptValidDays.setEditable(true);
				radBtnNo.setSelected(false);
			}
		}
	}

	@FXML
	public void setDiagTxtFieldsNoEdit (MouseEvent mouseEvent) throws IOException {

		if(radBtnDiagNo.isSelected()){
			inDiagnosisName.setEditable(false);
			inDiagnosisSev.setDisable(true);
			radBtnDiagYes.setSelected(false);
		}
	}

	@FXML
	public void setDiagTxtFieldsEdit (MouseEvent mouseEvent) throws IOException {
		String role = UserSession.getInstance().getRole();
		if(radBtnDiagYes.isSelected()){
			if ("Nurse".equals(role)) {
				Alert alert = new Alert(Alert.AlertType.WARNING, "Sorry, only users with role type doctor can add diagnose patients", ButtonType.OK);
				alert.setTitle("Role Check");
				alert.setHeaderText(null);
				alert.showAndWait();
				radBtnDiagYes.setSelected(false);
				radBtnDiagNo.setSelected(true);
			} else {
				inDiagnosisName.setEditable(true);
				inDiagnosisSev.setDisable(false);
				radBtnDiagNo.setSelected(false);
			}
		}
	}

	@FXML
	public void setDiagIdTxtFieldsNoEdit (MouseEvent mouseEvent) throws IOException {

		if(radBtnDiagIdNo.isSelected()){
			inDiagnosisId.setEditable(false);
			radBtnDiagIdYes.setSelected(false);
		}
	}

	@FXML
	public void setDiagIdTxtFieldsEdit (MouseEvent mouseEvent) throws IOException {

		if(radBtnDiagIdYes.isSelected()){
			inDiagnosisId.setEditable(true);
			radBtnDiagIdNo.setSelected(false);
		}
	}

	@FXML	
	public void highlightPatientDirectoryPane(MouseEvent mouseEvent) throws IOException {
		patientDirectoryIVPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightPatientDirectoryPaneOnExit(MouseEvent mouseEvent) throws IOException {
		patientDirectoryIVPane.setStyle("-fx-background-color:  #063847");
	}
	
	@FXML	
	public void highlightAppointmentsPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		appointmentsDBPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightAppointmentsPaneOnExit(MouseEvent mouseEvent) throws IOException {
		appointmentsDBPane.setStyle("-fx-background-color:  #063847");
	}
	
	@FXML	
	public void highlightPatientNotesPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		patientNotesDBPane.setStyle("-fx-background-color: #02181f");
	}
	
	
	
	@FXML	
	public void highlightIVDashboardPaneOnEnter(MouseEvent mouseEvent) throws IOException {
		dashboardIVPane.setStyle("-fx-background-color: #02181f");
	}
	
	@FXML	
	public void highlightIVDashboardPaneOnExit(MouseEvent mouseEvent) throws IOException {
		dashboardIVPane.setStyle("-fx-background-color:  #063847");
	}
}
