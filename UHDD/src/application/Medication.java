package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * This is used in the dashboard, patient info view and encrypt PDF
 * @author User
 *
 */
public class Medication {
    private final SimpleIntegerProperty scriptId;
    private final SimpleIntegerProperty patientId;
    private final SimpleStringProperty medicationName;
    private final ObjectProperty<LocalDate> prescribedDate;
    private final ObjectProperty<LocalDate> expiredDate;

    /**
     * This is used in the dashboard, patient info view and encrypt PDF
     * @param scriptId
     * @param patientId
     * @param medicationName
     * @param prescribedDate
     * @param expiredDate
     */
    public Medication(int scriptId, int patientId, String medicationName, LocalDate prescribedDate, LocalDate expiredDate) {
        this.scriptId = new SimpleIntegerProperty(scriptId);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.medicationName = new SimpleStringProperty(medicationName);
        this.prescribedDate = new SimpleObjectProperty<>(prescribedDate);
        this.expiredDate = new SimpleObjectProperty<>(expiredDate);
    }

    /**
     * This is only used in this class
     * @return
     */
    public int getScriptId() {
        return scriptId.get();
    }

    /**
     * This is only used in this class
     * @return
     */
    public void setScriptId(int scriptId) {
        this.scriptId.set(scriptId);
    }

    /**
     * This is only used in this class
     * @return
     */
    public int getPatientId() {
        return patientId.get();
    }

    /**
     * This is only used in this class
     * @return
     */
    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
    }

    /**
     * This is only used in this class
     * @return
     */
    public String getMedicationName() {
        return medicationName.get();
    }

    /**
     * This is only used in this class
     * @return
     */
    public void setMedicationName(String medicationName) {
        this.medicationName.set(medicationName);
    }

    /**
     * This is only used in this class
     * @return
     */
    public LocalDate getPrescribedDate() {
        return prescribedDate.get();
    }

    /**
     * This is only used in this class
     * @return
     */
    public void setPrescribedDate(LocalDate prescribedDate) {
        this.prescribedDate.set(prescribedDate);
    }

    /**
     * This is only used in this class
     * @return
     */
    public LocalDate getExpiredDate() {
        return expiredDate.get();
    }

    /**
     * This is only used in this class
     * @return
     */
    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate.set(expiredDate);
    }
}
