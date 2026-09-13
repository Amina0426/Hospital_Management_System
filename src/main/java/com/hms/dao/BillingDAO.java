package com.hms.dao;

import com.hms.DBConnection;
import com.hms.entities.Billing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillingDAO {

    public boolean addBill(Billing b) {
        String sql = "INSERT INTO bills (patient_id, amount, bill_date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getPatient().getPatientId());
            ps.setDouble(2, b.getAmount());
            ps.setDate(3, Date.valueOf(b.getBillDate()));

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Failed to insert bill: " + e.getMessage());
            return false;
        }
    }

    public List<Billing> getAllBills() {
        List<Billing> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Billing b = new Billing(
                        rs.getInt("bill_id"),
                        null, // Patient object can be fetched via PatientDAO if needed
                        rs.getDouble("amount"),
                        rs.getDate("bill_date").toLocalDate());
                bills.add(b);
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch bills: " + e.getMessage());
        }
        return bills;
    }

    /** ✅ Get bill by ID */
    public Optional<Billing> getBillById(int id) {
        String sql = "SELECT * FROM bills WHERE bill_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Billing b = new Billing(
                            rs.getInt("bill_id"),
                            null,
                            rs.getDouble("amount"),
                            rs.getDate("bill_date").toLocalDate());
                    return Optional.of(b);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch bill by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    /** ✅ Update bill details (amount/date) */
    public boolean updateBill(Billing b) {
        String sql = "UPDATE bills SET patient_id=?, amount=?, bill_date=? WHERE bill_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getPatient().getPatientId());
            ps.setDouble(2, b.getAmount());
            ps.setDate(3, Date.valueOf(b.getBillDate()));
            ps.setInt(4, b.getBillId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Failed to update bill: " + e.getMessage());
            return false;
        }
    }

    /** ✅ Delete bill by ID */
    public boolean deleteBill(int id) {
        String sql = "DELETE FROM bills WHERE bill_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete bill: " + e.getMessage());
            return false;
        }
    }
}
