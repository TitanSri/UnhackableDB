package application.viewControllers;

/**
 * This is used for the patient directory, login controller, dashboard and patient info view 
 * @author User
 *
 */
public class CurrentFXMLInstance {

    private static CurrentFXMLInstance instance;

    private String currentFXML;

    private CurrentFXMLInstance(String currentFXML) {
        this.currentFXML = currentFXML;
    }

    /**
     * This is used for the user session and login controller
     * @param currentFXML
     */
    public static void initInstance(String currentFXML) {
        if(instance == null) {
            instance = new CurrentFXMLInstance(currentFXML);
        }
    }

    /**
     * This is used for the directory, login controller, dashboard and patient info view
     * @return
     */
    public static CurrentFXMLInstance getInstance() {
        return instance;
    }

    /**
     * This is used for patient info view and patient directory
     * @return
     */
    public String getCurrentFXML() {
        return currentFXML;
    }

    /**
     * This is used for patient directory, dashboard and patient info view 
     * @param currentFXML
     */
    public void setCurrentFXML(String currentFXML) {
        this.currentFXML = currentFXML;
    }

    /**
     * This is only used in this class
     */
    public void cleanCurrentFXMLInstance() {
        instance = null;
    }
}
