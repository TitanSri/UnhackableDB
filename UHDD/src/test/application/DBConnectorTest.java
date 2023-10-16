package application;
/*
package application.test.application;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.mysql.cj.protocol.Resultset;

import application.DBConnector;
import application.PasswordHash;

@TestMethodOrder(MethodOrderer.MethodName.class)
class DBConnectorTest {
	static DBConnector db = new DBConnector();
	String title;
	String id;
	Boolean fullDay;
	LocalDate startDate;
	LocalDate endDate;
	LocalTime startTime;
	LocalTime endTime;
	ZoneId zoneId;
	Boolean recurring;
	String rRule;
	Boolean recurrence;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		db.initialiseDB();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void dbConnectorLoggedInTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby")); 
		db.setLoggedInStatus("toby", 1);
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby"));
		
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby"));
		db.setLoggedInStatus("toby", 0);
		System.out.println("Logged in status for user: " + db.getLoggedInStatus("toby"));
		System.out.println("Is user logged in? " + db.isAnyUserLoggedIn("toby"));
	}
	
	@Test
	void dbCreateUserTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		db.createUserExecuteQuery("junit", "testHash", "testpara", "test@.com", "Doctor", "2023-06-20 22:28:03");
	}
	
	@Test
	void dbChangePasswordTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		db.changePasswordExecuteQuery("junit", "newTestHash", "newTestPara", "2023-07-20 22:28:03");
	}
	
	@Test
	void dbQueryResultsetUserTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		try (ResultSet userDetails = db.QueryReturnResultsFromUser("junit")) {
			if (userDetails.next()) {
				// Retrieve the "password_hash" from the database
				String passwordHash = userDetails.getString("password_hash");
				System.out.println("User password hash: " + passwordHash);
				
			} else {
				// The user was not found in the database
				System.out.println("User not found");
			}
		} catch (SQLException e) {
			// Handle the exception that occurred while accessing the database
		}
	}
	
	@Test
	void dbQueryResultsetIdTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		try (ResultSet userDetails = db.QueryReturnResultsFromPatientId("1")) {
			if (userDetails.next()) {
				// Retrieve the "password_hash" from the database
				String fname = userDetails.getString("FirstName");
				System.out.println("First name of ID number 1: " + fname);
				
			} else {
				// The user was not found in the database
				System.out.println("User not found");
			}
		} catch (SQLException e) {
			// Handle the exception that occurred while accessing the database
		}
	}
	
	@Test
	void dbQueryResultsetNameTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		try (ResultSet userDetails = db.QueryReturnResultsFromPatientName("Michael Jordan")) {
			if (userDetails.next()) {
				// Retrieve the "password_hash" from the database
				String address = userDetails.getString("Address");
				System.out.println("Address of Michael Jordan: " + address);
			} else {
				// The user was not found in the database
				System.out.println("User not found");
			}
		} catch (SQLException e) {
			// Handle the exception that occurred while accessing the database
		}
	}
	
	@Test
	void dbAddCalendarEventTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		id = "e13d4041-182b-4c88-ad6b-5acee65089d9";
		startDate = LocalDate.parse("2023-07-09");
		endDate = LocalDate.parse("2023-07-09");
		startTime = LocalTime.parse("18:30"); 
		endTime = LocalTime.parse("18:45");
		zoneId = ZoneId.of("Australia/Sydney");
		fullDay = false;
		recurrence = false;
		rRule = "FREQ=DAILY;COUNT=2;INTERVAL=2";
		recurring = false;
		db.addCalendarEvent("JUnit", id, fullDay, startDate, endDate, startTime, endTime, zoneId, recurrence, rRule, recurring);
	}
	
	@Test
	void dbGetCalendarEventTest() throws ClassNotFoundException, SQLException {
//		fail("Not yet implemented");
		db.getCalendarEvents();
	}
		

}
*/