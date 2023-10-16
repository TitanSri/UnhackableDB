package application;

/**
 * This is used in the login controller, patient info view, dashboard and directory
 * @author User
 *
 */
public class PatientService {
    private static PatientService instance = new PatientService();
    private Patient currentPatient;

    private PatientService() {}

    /**
     * This is used in the encryption controller, and decryptor
     * @return
     */
    public static PatientService getInstance() {
        return instance;
    }

    /**
     * This is used in the patient info controller
     * @return
     */
    public Patient getCurrentPatient() {
        return currentPatient;
    }

    /**
     * This is used in the login controller and directory
     * @param currentPatient
     */
    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }
}
