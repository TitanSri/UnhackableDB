package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This is used in the patient service, patient info view, dashboard
 * encrypted PDF, patient directory, login controller, and UI
 * @author User
 *
 */
public class Patient {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty familyName;
    private final SimpleStringProperty givenName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty address;
    private final SimpleStringProperty city;
    private final SimpleStringProperty state;
    private final SimpleStringProperty telephone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty healthInsuranceNumber;
    private final SimpleStringProperty emergencyContactNumber;

    public Patient(int id, String familyName, String givenName) {
        this.id = new SimpleIntegerProperty(id);
        this.familyName = new SimpleStringProperty(familyName);
        this.givenName = new SimpleStringProperty(givenName);
        this.middleName = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.telephone = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.dateOfBirth = new SimpleStringProperty("");
        this.healthInsuranceNumber = new SimpleStringProperty("");
        this.emergencyContactNumber = new SimpleStringProperty("");
    }

    public Patient(int id, String familyName, String givenName, String middleName, String gender, String address, String city, String state, String telephone, String email, String dateOfBirth, String healthInsuranceNumber, String emergencyContactNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.familyName = new SimpleStringProperty(familyName);
        this.givenName = new SimpleStringProperty(givenName);
        this.middleName = new SimpleStringProperty(middleName);
        this.gender = new SimpleStringProperty(gender);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.telephone = new SimpleStringProperty(telephone);
        this.email = new SimpleStringProperty(email);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
        this.healthInsuranceNumber = new SimpleStringProperty(healthInsuranceNumber);
        this.emergencyContactNumber = new SimpleStringProperty(emergencyContactNumber);
    }

    /**
     * This is used in the dashboard, patient info view and the directory 
     * @return
     */
    public int getId() {
        return id.get();
    }

    /**
     * This is used in the DB conenctor
     * @param id
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * This is used in the dashboard, patient info view and directory
     * @return
     */
    public String getFamilyName() {
        return familyName.get();
    }

    /**
     * This is only used in this class
     * @param familyName
     */
    public void setFamilyName(String familyName) {
        this.familyName.set(familyName);
    }

    /**
     * This is used in the dashboard, patient info view and directory
     * @return
     */
    public String getGivenName() {
        return givenName.get();
    }

    /**
     * This is only used in this class
     * @param givenName
     */
    public void setGivenName(String givenName) {
        this.givenName.set(givenName);
    }

    /**
     * This is used in the patient info view
     * @return
     */
    public String getMiddleName() {
        return middleName.get();
    }

    /**
     * This is only used in this class
     * @param middleName
     */
    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    /**
     * This is used in the patient info view
     * @return
     */
    public String getGender() {
        return gender.get();
    }

    /**
     * This is only used in this class
     * @param gender
     */
    public void setGender(String gender) {
        this.gender.set(gender);
    }

    /**
     * This is used in the patient info view
     * @return
     */
    public String getAddress() {
        return address.get();
    }

    /**
     * This is only used in this class
     * @param address
     */
    public void setAddress(String address) {
        this.address.set(address);
    }

    /**
     * This is used in the patient info view
     * @return
     */
    public String getCity() {
        return city.get();
    }

    /**
     * This is only used in this class
     * @param city
     */
    public void setCity(String city) {
        this.city.set(city);
    }

    /**
     * This is only used in this class
     * @return
     */
    public String getState() {
        return state.get();
    }

    /**
     * This is only used in this class
     * @param state
     */
    public void setState(String state) {
        this.state.set(state);
    }

    /**
     * THis is used in the patient info view
     * @return
     */
    public String getTelephone() {
        return telephone.get();
    }

    /**
     * This is only used in this class
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    /**
     * This is used in the password reset and patient info view
     * @return
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * This is only used in this class
     * @param email
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * This is used in the patient info view
     * @return
     */
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    /**
     * This is only used in this class
     * @param dateOfBirth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    /**
     * This is used in the patient info view
     * @return
     */
    public String getHealthInsuranceNumber() {
        return healthInsuranceNumber.get();
    }

    /**
     * This is only used in this class
     * @param healthInsuranceNumber
     */
    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        this.healthInsuranceNumber.set(healthInsuranceNumber);
    }

    /**
     * This is only used in this class
     * @return
     */
    public String getEmergencyContactNumber() {
        return emergencyContactNumber.get();
    }

    /**
     * This is used in the patient info view
     * @return
     */
    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber.set(emergencyContactNumber);
    }
}

