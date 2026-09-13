package com.hms.dao;

import com.hms.DBConnection;
import com.hms.entities.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDAO {

    public boolean addPatient(Patient p) {
        String sql = "INSERT INTO patients (patient_id, name, age, gender, phone_number, address, illness, doctor_id, admitted, admit_date, billing_amt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getPatientId());
            ps.setString(2, p.getFullName());
            ps.setInt(3, p.getAge());
            ps.setString(4, p.getGender());
            ps.setString(5, p.getPhoneNumber());
            ps.setString(6, p.getAddress());
            ps.setString(7, p.getIllness());
            if (p.getDoctorId() != null) {
                ps.setString(8, p.getDoctorId());
            } else {
                ps.setNull(8, java.sql.Types.VARCHAR);
            }

            ps.setBoolean(9, p.isAdmitted());
            if (p.getAdmitDate() != null && !p.getAdmitDate().isEmpty()) {
                ps.setDate(10, Date.valueOf(p.getAdmitDate()));
            } else {
                ps.setNull(10, java.sql.Types.DATE);
            }

            ps.setDouble(11, p.getBillingAmt());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("? Failed to insert patient: " + e.getMessage());
            return false;
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch patients: " + e.getMessage());
        }
        return patients;
    }

    public Optional<Patient> getPatientById(String id) {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapPatient(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch patient: " + e.getMessage());
        }
        return Optional.empty();
    }

    // ✅ New reusable method
    private Patient mapPatient(ResultSet rs) throws SQLException {
        // Handle admit_date safely
        String admitDate = null;
        Date sqlDate = rs.getDate("admit_date");
        if (sqlDate != null) {
            admitDate = sqlDate.toString();
        }

        // doctor_id can be null too
        String doctorId = rs.getString("doctor_id"); // will be null if no doctor assigned

        return new Patient(
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("patient_id"),
                rs.getString("illness"),
                doctorId,
                rs.getBoolean("admitted"),
                admitDate,
                rs.getDouble("billing_amt"));
    }

    public boolean deletePatientById(String id) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("? Failed to delete patient: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePatientInfo(Patient p) {
        String sql = "UPDATE patients SET name=?, age=?, gender=?, phone_number=?, address=?, illness=?, doctor_id=?, admitted=?, admit_date=?, billing_amt=? WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getFullName());
            ps.setInt(2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setString(4, p.getPhoneNumber());
            ps.setString(5, p.getAddress());
            ps.setString(6, p.getIllness());
            ps.setString(7, p.getDoctorId());
            ps.setBoolean(8, p.isAdmitted());
            ps.setDate(9, Date.valueOf(p.getAdmitDate()));
            ps.setDouble(10, p.getBillingAmt());
            ps.setString(11, p.getPatientId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("? Failed to update patient: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDoctorForPatient(String patientId, String doctorId) {
        String sql = "UPDATE patients SET doctor_id = ? WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorId);
            ps.setString(2, patientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update doctor for patient: " + e.getMessage());
            return false;
        }
    }

}
