package com.hms.services;

import com.hms.DBConnection;
import com.hms.dao.AdminDAO;
import com.hms.entities.Admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class AdminService {
    private final AdminDAO adminDAO = new AdminDAO();

    public boolean addAdmin(Admin a) {
        return adminDAO.addAdmin(a);
    }

    public List<Admin> getAllAdmins() {
        return adminDAO.getAllAdmins();
    }

    public Optional<Admin> getAdminById(String adminId) {
        return adminDAO.getAdminById(adminId);
    }

    public boolean updateAdmin(Admin a) {
        return adminDAO.updateAdmin(a);
    }

    public boolean deleteAdmin(String adminId) {
        return adminDAO.deleteAdmin(adminId);
    }

    public Optional<Admin> authenticate(String username, String password) {
        return getAllAdmins().stream()
                .filter(a -> a.getUserName().equals(username) && a.getPassword().equals(password))
                .findFirst();
    }

    public static int getNextNumber() {
        String sql = "SELECT admin_id FROM admins ORDER BY admin_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("admin_id"); // e.g., "AD1001"
                String numPart = lastId.replaceAll("[^0-9]", "");
                return Integer.parseInt(numPart) + 1;
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch last admin ID: " + e.getMessage());
        }
        return 1; // default if table empty
    }
}
