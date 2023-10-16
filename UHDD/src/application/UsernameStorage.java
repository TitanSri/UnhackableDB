package application;

/**
 * This may be redundant 
 * @author User
 *
 */
public class UsernameStorage {
    private static String username;

    public static void setUsername(String name) {
        username = name;
    }

    public static String getUsername() {
        return username;
    }
}