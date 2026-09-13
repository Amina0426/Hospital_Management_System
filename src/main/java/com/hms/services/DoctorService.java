package com.hms.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import com.hms.DBConnection;
import com.hms.dao.DoctorDAO;
import com.hms.entities.Doctor;

public class DoctorService {
    private final DoctorDAO doctorDAO = new DoctorDAO();

    public boolean addDoctor(Doctor d) {
        return doctorDAO.addDoctor(d);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    public Optional<Doctor> getDoctorById(String doctorId) {
        return doctorDAO.getById(doctorId);
    }

    public boolean updateDoctor(Doctor d) {
        return doctorDAO.updateDoctor(d);
    }

    public boolean deleteDoctor(String doctorId) {
        return doctorDAO.deleteDoctor(doctorId);
    }

    public List<Doctor> getDoctorBySpecialization(String specialization) {
        return doctorDAO.getBySpecialization(specialization);
    }

    public List<Doctor> getDoctorByAvailability() {
        return doctorDAO.getByAvailability();
    }

    public Optional<Doctor> authenticate(String username, String password) {
        return getAllDoctors().stream()
                .filter(a -> a.getUserName().equals(username) && a.getPassword().equals(password))
                .findFirst();
    }

    public static int getNextNumber() {
        String sql = "SELECT doctor_id FROM doctors ORDER BY doctor_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("doctor_id"); // e.g., "DOC12"
                // Extract numeric part
                String numPart = lastId.replaceAll("[^0-9]", "");
                return Integer.parseInt(numPart) + 1;
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch last doctor ID: " + e.getMessage());
        }
        return 1; // default if no doctors exist
    }
}
