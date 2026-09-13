package com.hms.services;

import com.hms.DBConnection;
import com.hms.dao.ReceptionistDAO;
import com.hms.entities.Receptionist;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ReceptionistService {
    private final ReceptionistDAO recDAO = new ReceptionistDAO();

    public boolean addReceptionist(Receptionist r) {
        return recDAO.addReceptionist(r);
    }

    public List<Receptionist> getAllReceptionists() {
        return recDAO.getAllReceptionists();
    }

    public Optional<Receptionist> getReceptionistById(String recId) {
        return recDAO.getReceptionistById(recId);
    }

    public boolean updateReceptionist(Receptionist r) {
        return recDAO.updateReceptionist(r);
    }

    public boolean deleteReceptionist(String recId) {
        return recDAO.deleteReceptionist(recId);
    }

    public Optional<Receptionist> authenticate(String username, String password) {
        return getAllReceptionists().stream()
                .filter(a -> a.getUserName().equals(username) && a.getPassword().equals(password))
                .findFirst();
    }

    public static int getNextNumber() {
        String sql = "SELECT receptionist_id FROM receptionists ORDER BY receptionist_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("receptionist_id"); // e.g., "REC12"
                String numPart = lastId.replaceAll("[^0-9]", "");
                return Integer.parseInt(numPart) + 1;
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch last receptionist ID: " + e.getMessage());
        }
        return 1; // default if table empty
    }
}
