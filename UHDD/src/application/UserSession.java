package application;

/**
 * This is used in the password reset, login controller, patient info view
 * dashbaord, main and directory 
 * @author User
 *
 */
public class UserSession {

    private static UserSession instance;

    private static String userName;
    private static String role; // Added role member variable
    private static String id;      // Added id member variable

    public UserSession() { 
    }
    
    public UserSession(String userName, String role, String id) { // Modified constructor
        this.userName = userName;
        this.role = role;
        this.id = id;
    }

    /**
     * This is used in the login controller
     * @param userName
     * @param role
     * @param id
     */
    public static void initInstance(String userName, String role, String id) { // Modified initInstance method
        if(instance == null) {
            instance = new UserSession(userName, role, id);
        }
    }

    /**
     * This is used in the encryption controller and decryptor
     * @return
     */
    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserSession has not been initialized.");
        }
        return instance;
    }

    /**
     * This is used in the main, password reset controller, dashboard, login controller and patient info view
     * @return
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * This is used in the patient directory and patient info view
     * @return
     */
    public String getRole() { // Getter for role
        return role;
    }

    /**
     * This is used in the dashbaord, patient info view and directory 
     * @return
     */
    public String getId() { // Getter for id
        return id;
    }

    /**
     * This is only used in this class
     */
    public void cleanUserSession() {
        userName = ""; // or null
        role = "";     // or null
        id = "";        // or any default value
        instance = null;
    }

    /**
     * This is used in the login controller
     * @param userName
     */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}