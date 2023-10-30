package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.calendarfx.model.Entry;
import com.google.zxing.Result;

/**
 * This class was created to handle all the queries to the mySQL database
 * @author Team 5
 *
 */
public class DBConnector {
	// these objects will be used in querying the database and processing the results
		public Connection connection; // the controllers needs to use this connection 
		String nextA;
		String nextTitle;
		String nextId;
		Boolean nextFullDay;
		LocalDate nextStartDate;
		LocalDate nextEndDate;
		LocalTime nextStartTime;
		LocalTime nextEndTime;
		ZoneId nextZoneId;
		Boolean nextRecurring;
		String nextRRule;
		Boolean nextRecurrence;		
		
		/**
		 * The initialise DB connected to Azure with an encrypted password
		 * The connection is used in the dashboard, login controller, patient directory
		 * patient info view, encryption, credential manger and main
		 * @throws Exception
		 */
		public void initialiseDB() throws Exception {
		
			// loads and checks the driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded:");
		
			// connection for database...make sure the URL is correct JDBC:MYSQL
			// connect to Jonathan's cloud
//			String url="jdbc:mysql://itc303-db01.mysql.database.azure.com:3306/testdb?useSSL=true";
			String url="jdbc:mysql://db-mysql.c2rdrl5sxil5.ap-southeast-2.rds.amazonaws.com:3306/UHDD?useSSL=true";
			String username = "admin";
//			String password = Decryptor.decrypt("HTTjDc5keIHeQ48mt3A+Vw=="); // Hash this password using a function and unhash it when it's called for this line so its not stored in plain text (makes it harder for people to access DB)
			String password = Decryptor.decrypt("VVXyG10QiL0ZdLVS8Zk/to1UsNDZ7UBaH7kyTXwxcH4=");
		
			// connect to the database
			try {
				connection = DriverManager.getConnection(url, username, password);
				System.out.println("Database connected:");
				
				connection.createStatement();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			} 
				
		}
		
		/**
		 * This will close the connection to the database 
		 * @throws SQLException
		 */
		public void closeConnection() throws SQLException {
	        if (connection != null && !connection.isClosed()) {
	            connection.close();
	        }
	    }

	    public Connection getConnection() {
	        return connection;
	    }
		
		/**
		 * This will change the login status in the main. login controller and the dashboard
		 * @param username
		 * @param loggedIn
		 * @throws SQLException
		 */
	    public void setLoggedInStatus(String username, int loggedIn) throws SQLException {
	        String sql = "UPDATE user_details SET logged_in = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, loggedIn);
	        statement.setString(2, username);
	        statement.executeUpdate();
	    }
	    
	    /**
	     * This will set the login time in the login controller and dashboard
	     * @param username
	     * @param timestamp
	     * @throws SQLException
	     */
	    public void setLastLoggedInTime(String username, Timestamp timestamp) throws SQLException {
	        String sql = "UPDATE user_details SET last_logged_in_date = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setTimestamp(1, timestamp);
	        statement.setString(2, username);
	        statement.executeUpdate();
	    }
	    /**
	     * This will get the login status in the login controller and dashboard
	     * @param username
	     * @return
	     * @throws SQLException
	     */
	    public int getLoggedInStatus(String username) throws SQLException {
	        int loggedInStatus = -1;
	        String sql = "SELECT logged_in FROM user_details WHERE username = ?";

	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, username);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    loggedInStatus = resultSet.getInt("logged_in");
	                }
	            }
	        }
	        return loggedInStatus;
	    }
	    
	    /**
	     * This is used in the dasboard
	     * @param username
	     * @return
	     * @throws SQLException
	     */
	    public Timestamp getLastLoggedInDate(String username) throws SQLException {
	        Timestamp lastLoggedInDate = null;
	        String sql = "SELECT last_logged_in_date FROM user_details WHERE username = ?";

	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, username);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    lastLoggedInDate = resultSet.getTimestamp("last_logged_in_date");
	                }
	            }
	        }
	        return lastLoggedInDate;
	    }

	    /**
	     * This is only used in this class
	     * @throws SQLException
	     */
	    public void checkAndSetLoggedOutStatus() throws SQLException {
	        String sql = "SELECT username, logged_in, last_logged_in_date FROM user_details WHERE logged_in = 1";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        ResultSet resultSet = statement.executeQuery();
	        List<String> usersToUpdateStatus = new ArrayList<>();

	        while (resultSet.next()) {
	            String username = resultSet.getString("username");
	            int loggedInStatus = resultSet.getInt("logged_in");
	            Timestamp lastLoggedInDate = resultSet.getTimestamp("last_logged_in_date");

	            LocalDateTime currentDateTime = LocalDateTime.now();
	            Timestamp loginTimestamp = Timestamp.valueOf(currentDateTime);

	            if (loggedInStatus == 1) {
	                if (lastLoggedInDate == null) {
	                    // Update last logged-in date to current date and time
	                    updateLastLoggedInDateAndStatus(username, loginTimestamp);
	                } else {
	                    // Check if the user has exceeded 5 minutes of inactivity
	                    Duration timeDifference = Duration.between(lastLoggedInDate.toLocalDateTime(), currentDateTime);
	                    long hoursDifference = timeDifference.toHours();

	                    if (hoursDifference > 5) {
	                        usersToUpdateStatus.add(username);
	                    }
	                }
	            } else {
	                // User has a logged_in status of 0, so we can skip them for inactivity check
	            }
	        }

	        // Update the status for users who have exceeded 5 minutes of inactivity
	        if (!usersToUpdateStatus.isEmpty()) {
	            String updateSql = "UPDATE user_details SET logged_in = 0 WHERE username IN (";
	            for (int i = 0; i < usersToUpdateStatus.size(); i++) {
	                updateSql += "?";
	                if (i < usersToUpdateStatus.size() - 1) {
	                    updateSql += ",";
	                }
	            }
	            updateSql += ")";

	            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
	            for (int i = 0; i < usersToUpdateStatus.size(); i++) {
	                updateStatement.setString(i + 1, usersToUpdateStatus.get(i));
	            }
	            updateStatement.executeUpdate();
	        }
	    }
	    
	    /**
	     * This is only used in this class
	     * @param username
	     * @param loginTimestamp
	     * @throws SQLException
	     */
	    public void updateLastLoggedInDateAndStatus(String username, Timestamp loginTimestamp) throws SQLException {
	        String sql = "UPDATE user_details SET last_logged_in_date = ?, logged_in = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setTimestamp(1, loginTimestamp);
	        statement.setInt(2, 1); // Set logged_in status to 1 (logged in)
	        statement.setString(3, username);
	        statement.executeUpdate();
	    }
	    
	    /**
	     * This is only used in this class
	     * @param username
	     * @throws SQLException
	     */
	    public void updateLastLoggedInDate(String username) throws SQLException {
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        Timestamp loginTimestamp = Timestamp.valueOf(currentDateTime);

	        String sql = "UPDATE user_details SET last_logged_in_date = ? WHERE username = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setTimestamp(1, loginTimestamp);
	        statement.setString(2, username);
	        statement.executeUpdate();
	    }
	    
	    /**
	     * This is used in the patient directory
	     * @param sql
	     * @return
	     * @throws SQLException
	     */
		public ResultSet executeQueryReturnResults(String sql) throws SQLException {
	        Statement statement = connection.createStatement();
	        return statement.executeQuery(sql);
	    }

		/**
		 * This is used in the UI class
		 * @param sql
		 * @throws SQLException
		 */
		public void executeUpdate(String sql) throws SQLException {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
	    }
		
		/**
		 * This is used in the UI class
		 * @param sql
		 * @throws SQLException
		 */
		public void executeQuery(String sql) throws SQLException {
			Statement statement = connection.createStatement();
			statement.executeQuery(sql);
	    }
		
		/**
		 * This is used in this class and the credential manager
		 * @param username
		 * @param passwordHash
		 * @param params
		 * @param email
		 * @param role
		 * @param dateString
		 * @param OTPSecretKey
		 * @throws SQLException
		 */
		public void createUserExecuteQuery(String username, String passwordHash, String params, String email, String role, String dateString, String OTPSecretKey)
		  throws SQLException {
			String sql = "INSERT INTO user_details (username, password_hash, password_params, email, role, user_creation_date, password_last_modified, OTPSecretKey) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, passwordHash);
			statement.setString(3, params);
			statement.setString(4, email);
			statement.setString(5, role);
			statement.setString(6, dateString);
			statement.setString(7, dateString);
			statement.setString(8, OTPSecretKey);
			statement.executeUpdate();
			
		}
		
		/**
		 * This is used in the patient info view
		 * @param patientId
		 * @param doctorId
		 * @param noteText
		 * @param noteEnteredDate
		 * @param scriptIncluded
		 * @throws SQLException
		 */
		public void createNewNoteExecuteQuery(String patientId, String doctorId, String noteText, String noteEnteredDate, String scriptIncluded)
		  throws SQLException {
			String sql = "INSERT INTO UHDD.patient_notes (patientId, doctorId, noteText, noteEnteredDate, scriptIncluded) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			statement.setString(2, doctorId);
			statement.setString(3, noteText);
			statement.setString(4, noteEnteredDate);
			statement.setString(5, scriptIncluded);
			statement.executeUpdate();
			
		}
		
		/**
		 * This is used in the patient directory
		 * @param firstName
		 * @param middleName
		 * @param lastName
		 * @param gender
		 * @param address
		 * @param city
		 * @param state
		 * @param telephone
		 * @param email
		 * @param dateOfBirth
		 * @param healthInsuranceNumber
		 * @param emergencyContactNumber
		 * @throws SQLException
		 */
		public void createNewPatientExecuteQuery(
			    String firstName, String middleName, String lastName, String gender,
			    String address, String city, String state, String telephone, String email,
			    String dateOfBirth, String healthInsuranceNumber, String emergencyContactNumber,
			    String EncryptionKey, Connection connection) throws SQLException {
			    
			    String sql = "INSERT INTO UHDD.patient_data " +
			                 "(firstName, middleName, lastName, gender, address, city, state, " +
			                 "telephone, email, dateOfBirth, healthInsuranceNumber, emergencyContactNumber, EncryptionKey) " +
			                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			    
			    try (PreparedStatement statement = connection.prepareStatement(sql)) {
			        statement.setString(1, firstName);
			        statement.setString(2, middleName);
			        statement.setString(3, lastName);
			        statement.setString(4, gender);
			        statement.setString(5, address);
			        statement.setString(6, city);
			        statement.setString(7, state);
			        statement.setString(8, telephone);
			        statement.setString(9, email);
			        statement.setString(10, dateOfBirth);
			        statement.setString(11, healthInsuranceNumber);
			        statement.setString(12, emergencyContactNumber);
			        statement.setString(13, EncryptionKey);
			        
			        statement.executeUpdate();
			    }
			}

		/**
		 * This is used in the patient info view
		 * @param patientId
		 * @param medication_name
		 * @param prescribed_date
		 * @param expired_date
		 * @param noteId
		 * @param prescribedBy
		 * @throws SQLException
		 */
		public void createNewMedicationExecuteQuery(String patientId, String medication_name, String prescribed_date, String expired_date, String noteId, String prescribedBy)
		  throws SQLException {
			String sql = "INSERT INTO UHDD.medication_data (patientId, medication_name, prescribed_date, expired_date, noteId, prescribedBy) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			statement.setString(2, medication_name);
			statement.setString(3, prescribed_date);
			statement.setString(4, expired_date);
			statement.setString(5, noteId);
			statement.setString(6, prescribedBy);
			statement.executeUpdate();
			
		}
		
		/**
		 * This is used in the patient info view
		 * @param patientId
		 * @param diagnosisName
		 * @param diagnosisSeverity
		 * @param diagnosedDate
		 * @param diagnosingDrId
		 * @throws SQLException
		 */
		public void createNewDiagnosisExecuteQuery(String patientId, String diagnosisName, String diagnosisSeverity, String diagnosedDate, String diagnosingDrId)
			throws SQLException {
			String sql = "INSERT INTO UHDD.patient_diagnoses "
					+ "(patientId, diagnosisName, diagnosisSeverity, diagnosedDate, diagnosingDrId) "
					+ "VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			statement.setString(2, diagnosisName);
			statement.setString(3, diagnosisSeverity);
			statement.setString(4, diagnosedDate);
			statement.setString(5, diagnosingDrId);
			statement.executeUpdate();
		}
		
		/**
		 * This is used in the patient info view
		 * @param noteId
		 * @param diagnosisId
		 * @throws SQLException
		 */
		public void CreateNewNoteDiagnosisIdLink(String noteId, String diagnosisId) throws SQLException {
			String sql = "INSERT INTO UHDD.diagnosis_notes (diagnosisId, noteId) VALUES (?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, diagnosisId);
			statement.setString(2, noteId);
			statement.executeUpdate();
		}
		
		/**
		 * This is used in the DB connector and credential manager
		 * @param username
		 * @param passwordHash
		 * @param params
		 * @param dateString
		 * @throws SQLException
		 */
		public void changePasswordExecuteQuery(String username, String passwordHash, String params, String dateString)
				  throws SQLException {
					String sql = "UPDATE user_details SET password_hash = ?, password_params = ?, password_last_modified = ? WHERE username = ?";
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setString(1, passwordHash);
					statement.setString(2, params);
					statement.setString(3, dateString);
					statement.setString(4, username);
					statement.executeUpdate();
			
		}

		/**
		 * This is used in the patient info view
		 * @param diagnosisId
		 * @param initialDetails
		 * @param progressPlanGoal
		 * @param expectedRemediationDate
		 * @throws SQLException
		 */
		public void CreateNewProgressPlanExecuteQuery(String diagnosisId, String initialDetails, String progressPlanGoal, String expectedRemediationDate) throws SQLException{
			String sql = "INSERT INTO UHDD.progress_plan (diagnosisId, initialDetails, progressPlanGoal, expectedRemediationDate) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, diagnosisId);
			statement.setString(2, initialDetails);
			statement.setString(3, progressPlanGoal);
			statement.setString(4, expectedRemediationDate);
			statement.executeUpdate();
		}
		
		/**
		 * This is used in the patient info view
		 * @param diagnosisId
		 * @return
		 * @throws SQLException
		 */
		public boolean verifyDiagnosisIdExists(String diagnosisId) throws SQLException {
			String sql = "SELECT COUNT(*) FROM UHDD.patient_diagnoses WHERE diagnosisId = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, diagnosisId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
			return false;
		}
		
		/**
		 * This is used in the patient directory
		 * @param patientId
		 * @return
		 * @throws SQLException
		 */
		public boolean verifyPatientIdExists(String patientId) throws SQLException {
			String sql = "SELECT COUNT(*) FROM UHDD.patient_data WHERE patientId = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
			return false;
		}
		
		/** 
		 * This is used in the credential manager
		 * @param username
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnOTPSecretKeyFromUser(String username) throws SQLException {
			String sql = "SELECT OTPSecretKey FROM UHDD.user_details WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);

	        return statement.executeQuery();
	    }

		/**
		 * THis is used in this class and the crednetial manager
		 * @param username
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsFromUser(String username) throws SQLException {
			String sql = "SELECT * FROM UHDD.user_details WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);

	        return statement.executeQuery();
	    }

		/**
		 * This is used in the login controller
		 * @param username
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsForUserSession(String username) throws SQLException {
			// Updated SQL query to select only id, username, and role
			String sql = "SELECT id, username, role FROM UHDD.user_details WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
		
			return statement.executeQuery();
		}

		/**
		 * This is used in the dashboard and patient directoty
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsFromPatients() throws SQLException {
			String sql = "SELECT * FROM UHDD.patient_data";
			PreparedStatement statement = connection.prepareStatement(sql);
	        return statement.executeQuery();
	    }

		/**
		 * This is used in the dashbaord controller
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsFromMedication() throws SQLException {
			String sql = "SELECT * FROM UHDD.medication_data";
			PreparedStatement statement = connection.prepareStatement(sql);
	        return statement.executeQuery();
	    }
		
		/**
		 * This is used in this class and the patient info view for the next patient function
		 * @param patientId
		 * @return
		 * @throws SQLException
		 * @deprecated
		 */
		public ResultSet QueryReturnResultsFromPatientId(String patientId) throws SQLException {
			String sql = "SELECT * FROM UHDD..patient_data WHERE ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			System.out.println(sql);
	        return statement.executeQuery();
	    }
		
		/**
		 * This is used in the encrypted PDF and patient info view
		 * @param patientId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsMedicationFromPatientId(String patientId) throws SQLException {
			String sql = "SELECT * FROM UHDD.medication_data WHERE patientId = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			System.out.println(sql);
	        return statement.executeQuery();
	    }
		
		/**
		 * This is used in the patient info view
		 * @param patientId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsDiagnosisFromPatientId(String patientId) throws SQLException {
			String sql = "SELECT * FROM UHDD.patient_diagnoses WHERE patientId = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			System.out.println(sql);
	        return statement.executeQuery();
	    }
		
		/**
		 * This is used in the patient info view
		 * @param patientId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsNotesFromPatientId(String patientId) throws SQLException {
			String sql = "SELECT * FROM UHDD.patient_notes WHERE patientId = ? ORDER BY noteEnteredDate DESC";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			System.out.println(sql);
	        return statement.executeQuery();
	    }

		/**
		 * This is used in the encrypted PDF, login controller, dashbaord and patient directory
		 * @param patientId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsFromPatientDataId(String patientId) throws SQLException {
			String sql = "SELECT * FROM UHDD.patient_data WHERE patientId = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
	        return statement.executeQuery();
	    }

		/**
		 * This is used in the patient info view
		 * @param patientId
		 * @param doctorId
		 * @param noteText
		 * @param noteEnteredDate
		 * @param scriptIncluded
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryNoteIdForDiagnosis(String patientId, String doctorId, String noteText, String noteEnteredDate, String scriptIncluded) throws SQLException {
			String sql = "SELECT noteId FROM UHDD.patient_notes WHERE patientId = ? AND doctorId = ? AND noteText = ? AND noteEnteredDate = ? AND scriptIncluded = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			statement.setString(2, doctorId);
			statement.setString(3, noteText);
			statement.setString(4, noteEnteredDate);
			statement.setString(5, scriptIncluded);
			System.out.println(sql);
	        return statement.executeQuery();
	    }
		
		/**
		 * This is used in this class only
		 * @param patientId
		 * @param diagnosisName
		 * @param diagnosisSeverity
		 * @param diagnosedDate
		 * @param diagnosingDrId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryDiagnosisId(String patientId, String diagnosisName, String diagnosisSeverity, String diagnosedDate, String diagnosingDrId)
			throws SQLException {
			String sql = "SELECT diagnosisId FROM UHDD.patient_diagnoses WHERE patientId = ? AND diagnosisName = ? AND diagnosisSeverity = ? AND diagnosedDate = ? AND diagnosingDrId = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, patientId);
			statement.setString(2, diagnosisName);
			statement.setString(3, diagnosisSeverity);
			statement.setString(4, diagnosedDate);
			statement.setString(5, diagnosingDrId);
			return statement.executeQuery();
		}

		/**
		 * This is used in this class 
		 * @param patientName
		 * @return
		 * @throws SQLException
		 * @deprecated
		 */
		public ResultSet QueryReturnResultsFromPatientName(String patientName) throws SQLException {
			String sql = "SELECT * FROM UHDD.patient_data WHERE firstName like ? and lastName like ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			String[] name = patientName.split(" ");
			String fName = name[0];
			String lName = name[1];
			statement.setString(1, fName);
			statement.setString(2, lName);
			System.out.println(statement);
	        return statement.executeQuery();
	    }

		/**
		 * This is used in the patient info view
		 * @param diagnosisId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsDiagIdForProgressPlan(String diagnosisId) throws SQLException {
				String sql = "SELECT pd.diagnosisName,\n" + //
						"       pp.initialDetails,\n" + //
						"       pp.progressPlanGoal,\n" + //
						"       pp.expectedRemediationDate,\n" + //
						"       pn.noteText\n" + //
						"FROM patient_diagnoses pd\n" + //
						"JOIN progress_plan pp ON pd.diagnosisId = pp.diagnosisId\n" + //
						"JOIN diagnosis_notes dn ON pd.diagnosisId = dn.diagnosisId\n" + //
						"JOIN patient_notes pn ON dn.noteId = pn.noteId\n" + //
						"WHERE pd.diagnosisId = ?\n" + //
						"ORDER BY pn.noteEnteredDate DESC\n" + //
						"LIMIT 1;";
						PreparedStatement statement = connection.prepareStatement(sql);
						statement.setString(1, diagnosisId);
						return statement.executeQuery();

		}

		/**
		 * This is used in the patient info view
		 * @param diagnosisId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsDiagIdForMedicationNames(String diagnosisId) throws SQLException {
			String sql = "SELECT md.medication_name FROM medication_data md\n" + //
					"JOIN diagnosis_notes dn ON md.noteId = dn.noteId\n" + //
					"JOIN patient_diagnoses pd ON dn.diagnosisId = pd.diagnosisId\n" + //
					"WHERE pd.diagnosisId = ?;\n" + //
					"";
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setString(1, diagnosisId);
					return statement.executeQuery();
		}

		/**
		 * This is used in the patient info view
		 * @param patientId
		 * @return
		 * @throws SQLException
		 */
		public ResultSet QueryReturnResultsMostRecentDiagIdForPP(String patientId) throws SQLException {
			String sql = "SELECT pd.diagnosisId\n" + //
					"FROM patient_diagnoses pd\n" + //
					"JOIN progress_plan pp ON pd.diagnosisId = pp.diagnosisId\n" + //
					"WHERE pd.patientId = ?\n" + //
					"ORDER BY pp.progressPlanId DESC\n" + //
					"LIMIT 1;";
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setString(1, patientId);
					return statement.executeQuery();
		}
		
		/**
		 * This is used in this class and the dashbord 
		 * @param nextTitle
		 * @param nextId
		 * @param nextFullDay
		 * @param nextStartDate
		 * @param nextEndDate
		 * @param nextStartTime
		 * @param nextEndTime
		 * @param nextZoneId
		 * @param nextRecurring
		 * @param nextRRule
		 * @param nextRecurrence
		 * @throws SQLException
		 */
		public void addCalendarEvent(String nextTitle, String nextId, Boolean nextFullDay, LocalDate nextStartDate, 
				LocalDate nextEndDate, LocalTime nextStartTime,	LocalTime nextEndTime, ZoneId nextZoneId,
				Boolean nextRecurring, String nextRRule, Boolean nextRecurrence) throws SQLException {
			String sql = "INSERT INTO doctor_calendar (Title, Id, FullDay, StartDate, EndDate, StartTime, EndTime, ZoneId, Recurring, "
					+ "RRule, Recurrence) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, nextTitle);
			statement.setString(2, nextId);
			statement.setString(3, nextFullDay.toString());
			statement.setString(4, nextStartDate.toString());
			statement.setString(5, nextEndDate.toString());
			statement.setString(6, nextStartTime.toString());
			statement.setString(7, nextEndTime.toString());
			statement.setString(8, nextZoneId.toString());
			statement.setString(9, nextRecurring.toString());
			statement.setString(10, nextRRule);
			statement.setString(11, nextRecurrence.toString());
			statement.executeUpdate();
		}
		
		/**
		 * This is used in this class and the dashboard
		 * @throws SQLException
		 */
		public void getCalendarEvents() throws SQLException {
			// grabs all the rows from the doctor_calendar DB
			String query = "SELECT * FROM UHDD.doctor_calendar";
			ResultSet rs = this.executeQueryReturnResults(query);
			//Reduce noise
			//System.out.println(rs.getRow());
			while (rs.next()) {
				nextTitle = rs.getString("Title");
				nextId = rs.getString("Id");
				nextFullDay = Boolean.valueOf(rs.getString("FullDay"));
				nextStartDate = LocalDate.parse(rs.getString("StartDate"));
				nextEndDate = LocalDate.parse(rs.getString("EndDate"));
				nextStartTime = LocalTime.parse(rs.getString("StartTime"));
				nextEndTime = LocalTime.parse(rs.getString("StartTime"));
				nextZoneId = ZoneId.of(rs.getString("ZoneId"));
				nextRecurring = Boolean.valueOf(rs.getString("Recurring"));
				nextRRule = rs.getString("RRule");
				nextRecurrence = Boolean.valueOf(rs.getString("Recurrence"));
				/*
				 * Reduce noise whilst bug fixing
				 
				System.out.println("Entry from loop: " + nextTitle + ", " + nextId + ", " 
				+ nextFullDay + ", " + nextStartDate + ", " + nextEndDate + ", "
				+ nextStartTime + ", " + nextEndTime + "' " + nextZoneId + ", "
				+ nextRecurring + ", " + nextRRule + ", " + nextRecurrence);	
				*/
				// need to add amap list
				Map<LocalDate, List<Entry<?>>> newMapEntry = null;
				Entry newEntry = new Entry<Object>();
				newEntry.setTitle(nextTitle);
				newEntry.setId(nextId);
				newEntry.setFullDay(nextFullDay);
				newEntry.changeStartDate(nextStartDate);
				newEntry.changeEndDate(nextEndDate);
				newEntry.changeStartTime(nextStartTime);
				newEntry.changeEndTime(nextEndTime);
				newEntry.changeZoneId(nextZoneId);
				newEntry.setRecurrenceRule(nextRRule);
				
				//System.out.println("New Entry: " + newEntry);
				 // add the new entry to the doctor calendar
				 
				CalendarApp.getDoctors().addEntry(newEntry);
			} 
			
		}
		
}