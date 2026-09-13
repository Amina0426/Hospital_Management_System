package com.hms.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import com.hms.DBConnection;
import com.hms.dao.PatientDAO;
import com.hms.entities.Patient;

public class PatientService {
    private final PatientDAO patientDAO = new PatientDAO();

    public boolean addPatient(Patient p) {
        return patientDAO.addPatient(p);
    }

    public Optional<Patient> getPatientById(String patientId) {
        return patientDAO.getPatientById(patientId);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }

    public boolean removePatient(String patientId) {
        return patientDAO.deletePatientById(patientId);
    }

    public boolean updatePatientInfo(Patient p) {
        return patientDAO.updatePatientInfo(p);
    }

    public static int getNextNumber() {
        String sql = "SELECT patient_id FROM patients ORDER BY patient_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("patient_id"); // e.g., "PAT1001"
                String numPart = lastId.replaceAll("[^0-9]", "");
                return Integer.parseInt(numPart) + 1;
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch last patient ID: " + e.getMessage());
        }
        return 1; // default if table empty
    }

}