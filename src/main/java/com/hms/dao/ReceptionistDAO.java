package com.hms.dao;

import com.hms.DBConnection;
import com.hms.entities.Receptionist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReceptionistDAO {

    public boolean addReceptionist(Receptionist r) {
        String sql = "INSERT INTO receptionists (staff_id, receptionist_id, name, age, gender, phone_number, address, user_name, password, salary, shift_timing, desk_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getStaffId());
            ps.setString(2, r.getReceptionistId());
            ps.setString(3, r.getFullName());
            ps.setInt(4, r.getAge());
            ps.setString(5, r.getGender());
            ps.setString(6, r.getPhoneNumber());
            ps.setString(7, r.getAddress());
            ps.setString(8, r.getUserName());
            ps.setString(9, r.getPassword());
            ps.setDouble(10, r.getSalary());
            ps.setString(11, r.getShiftTiming());
            ps.setString(12, r.getDeskNumber());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("? Failed to insert receptionist: " + e.getMessage());
            return false;
        }
    }

    public List<Receptionist> getAllReceptionists() {
        List<Receptionist> receptionists = new ArrayList<>();
        String sql = "SELECT * FROM receptionists";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Receptionist r = new Receptionist(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("staff_id"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getDouble("salary"),
                        rs.getString("shift_timing"),
                        rs.getString("desk_number"),
                        rs.getString("receptionist_id"));
                receptionists.add(r);
            }
        } catch (SQLException e) {
            System.out.println("? Failed to fetch receptionists: " + e.getMessage());
        }
        return receptionists;
    }

    public Optional<Receptionist> getReceptionistById(String recId) {
        String sql = "SELECT * FROM receptionists WHERE receptionist_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapReceptionist(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch receptionist by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean updateReceptionist(Receptionist r) {
        String sql = "UPDATE receptionists SET name=?, age=?, gender=?, phone_number=?, address=?, user_name=?, password=?, salary=?, shift_timing=?, desk_number=? WHERE receptionist_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getFullName());
            ps.setInt(2, r.getAge());
            ps.setString(3, r.getGender());
            ps.setString(4, r.getPhoneNumber());
            ps.setString(5, r.getAddress());
            ps.setString(6, r.getUserName());
            ps.setString(7, r.getPassword());
            ps.setDouble(8, r.getSalary());
            ps.setString(9, r.getShiftTiming());
            ps.setString(10, r.getDeskNumber());
            ps.setString(11, r.getReceptionistId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Failed to update receptionist: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReceptionist(String recId) {
        String sql = "DELETE FROM receptionists WHERE receptionist_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete receptionist: " + e.getMessage());
            return false;
        }
    }

    // Utility method to convert a ResultSet row into a Receptionist object
    private Receptionist mapReceptionist(ResultSet rs) throws SQLException {
        return new Receptionist(
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("staff_id"),
                rs.getString("user_name"),
                rs.getString("password"),
                rs.getDouble("salary"),
                rs.getString("shift_timing"),
                rs.getString("desk_number"),
                rs.getString("receptionist_id"));
    }

}
