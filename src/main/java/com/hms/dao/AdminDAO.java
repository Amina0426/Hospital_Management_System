package com.hms.dao;

import com.hms.DBConnection;
import com.hms.entities.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminDAO {

    public boolean addAdmin(Admin a) {
        String sql = "INSERT INTO admins (staff_id, admin_id, name, age, gender, phone_number, address, user_name, password, salary, admin_level, department_assigned) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getStaffId());
            ps.setString(2, a.getAdminId());
            ps.setString(3, a.getFullName());
            ps.setInt(4, a.getAge());
            ps.setString(5, a.getGender());
            ps.setString(6, a.getPhoneNumber());
            ps.setString(7, a.getAddress());
            ps.setString(8, a.getUserName());
            ps.setString(9, a.getPassword());
            ps.setDouble(10, a.getSalary());
            ps.setInt(11, a.getAdminLevel());
            ps.setString(12, a.getDepartmentAssigned());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("⚠️ Failed to insert admin: " + e.getMessage());
            return false;
        }
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admins";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                admins.add(mapAdmin(rs));
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to fetch admins: " + e.getMessage());
        }
        return admins;
    }

    /** ✅ Fetch admin by ID */
    public Optional<Admin> getAdminById(String adminId) {
        String sql = "SELECT * FROM admins WHERE admin_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapAdmin(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to fetch admin: " + e.getMessage());
        }
        return Optional.empty();
    }

    /** ✅ Update existing admin */
    public boolean updateAdmin(Admin a) {
        String sql = "UPDATE admins SET name=?, age=?, gender=?, phone_number=?, address=?, user_name=?, password=?, salary=?, admin_level=?, department_assigned=?, staff_id=? "
                +
                "WHERE admin_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getFullName());
            ps.setInt(2, a.getAge());
            ps.setString(3, a.getGender());
            ps.setString(4, a.getPhoneNumber());
            ps.setString(5, a.getAddress());
            ps.setString(6, a.getUserName());
            ps.setString(7, a.getPassword());
            ps.setDouble(8, a.getSalary());
            ps.setInt(9, a.getAdminLevel());
            ps.setString(10, a.getDepartmentAssigned());
            ps.setString(11, a.getStaffId());
            ps.setString(12, a.getAdminId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("⚠️ Failed to update admin: " + e.getMessage());
            return false;
        }
    }

    /** ✅ Delete admin by ID */
    public boolean deleteAdmin(String adminId) {
        String sql = "DELETE FROM admins WHERE admin_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, adminId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("⚠️ Failed to delete admin: " + e.getMessage());
            return false;
        }
    }

    /** ✅ Centralized mapping */
    private Admin mapAdmin(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("staff_id"),
                rs.getString("user_name"),
                rs.getString("password"),
                rs.getDouble("salary"),
                rs.getString("admin_id"),
                rs.getInt("admin_level"),
                rs.getString("department_assigned"));
    }

}
