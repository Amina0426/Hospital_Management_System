package com.hms.dao;

import com.hms.DBConnection;
import com.hms.entities.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDAO {

    public boolean addDoctor(Doctor d) {
        String sql = "INSERT INTO doctors (staff_id, doctor_id, name, age, gender, phone_number, address, user_name, password, salary, specialization, department, availability, consultation_fee) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getStaffId());
            ps.setString(2, d.getDoctorId());
            ps.setString(3, d.getFullName());
            ps.setInt(4, d.getAge());
            ps.setString(5, d.getGender());
            ps.setString(6, d.getPhoneNumber());
            ps.setString(7, d.getAddress());
            ps.setString(8, d.getUserName());
            ps.setString(9, d.getPassword());
            ps.setDouble(10, d.getSalary());
            ps.setString(11, d.getSpecialization());
            ps.setString(12, d.getDepartment());
            ps.setBoolean(13, d.isAvailability());
            ps.setDouble(14, d.getConsultationFee());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("? Failed to insert doctor: " + e.getMessage());
            return false;
        }
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Doctor d = new Doctor(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("staff_id"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getDouble("salary"),
                        rs.getString("doctor_id"),
                        rs.getString("specialization"),
                        rs.getString("department"),
                        rs.getBoolean("availability"),
                        rs.getDouble("consultation_fee"));
                doctors.add(d);
            }
        } catch (SQLException e) {
            System.out.println("? Failed to fetch doctors: " + e.getMessage());
        }
        return doctors;
    }

    public List<Doctor> getBySpecialization(String spec) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialization = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, spec);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch doctors by specialization: " + e.getMessage());
        }
        return doctors;
    }

    public List<Doctor> getByAvailability() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE availability = TRUE";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch available doctors: " + e.getMessage());
        }
        return doctors;
    }

    public boolean deleteDoctor(String id) {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to delete doctor: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDoctor(Doctor d) {
        String sql = "UPDATE doctors SET name=?, age=?, gender=?, phone_number=?, address=?, user_name=?, password=?, salary=?, specialization=?, department=?, availability=?, consultation_fee=? WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getFullName());
            ps.setInt(2, d.getAge());
            ps.setString(3, d.getGender());
            ps.setString(4, d.getPhoneNumber());
            ps.setString(5, d.getAddress());
            ps.setString(6, d.getUserName());
            ps.setString(7, d.getPassword());
            ps.setDouble(8, d.getSalary());
            ps.setString(9, d.getSpecialization());
            ps.setString(10, d.getDepartment());
            ps.setBoolean(11, d.isAvailability());
            ps.setDouble(12, d.getConsultationFee());
            ps.setString(13, d.getDoctorId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Failed to update doctor: " + e.getMessage());
            return false;
        }
    }

    public Optional<Doctor> getById(String id) {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapDoctor(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch doctor by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Utility to map ResultSet -> Doctor
    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        return new Doctor(
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("staff_id"),
                rs.getString("user_name"),
                rs.getString("password"),
                rs.getDouble("salary"),
                rs.getString("doctor_id"),
                rs.getString("specialization"),
                rs.getString("department"),
                rs.getBoolean("availability"),
                rs.getDouble("consultation_fee"));
    }

}
