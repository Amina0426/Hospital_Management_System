package com.hms.services;

import java.util.List;

import com.hms.entities.Billing;
import com.hms.entities.Patient;
import com.hms.dao.BillingDAO;
import java.util.Optional;

public class BillingService {
    private final BillingDAO billDAO = new BillingDAO();

    // Create and persist a new bill
    public Billing createBill(Patient patient, double amount) {
        Billing bill = new Billing(0, patient, amount, java.time.LocalDate.now()); // 0 or auto-increment handled in DAO
        if (billDAO.addBill(bill)) {
            System.out.println("Bill created for " + patient.getFullName() + " Amount: $" + amount);
            return bill;
        }
        System.out.println("Failed to create bill.");
        return null;
    }

    // Retrieve bill by ID
    public Optional<Billing> getBillById(int billId) {
        return billDAO.getBillById(billId);
    }

    // Retrieve all bills
    public List<Billing> getAllBills() {
        return billDAO.getAllBills();
    }

    // Pay a bill and update in DB
    public boolean payBill(int billId, double amount) {
        Optional<Billing> opt = getBillById(billId);
        if (opt.isPresent()) {
            Billing b = opt.get();
            b.payBill(amount);
            return billDAO.updateBill(b);
        }
        return false;
    }

    // Delete a bill
    public boolean deleteBill(int billId) {
        return billDAO.deleteBill(billId);
    }

    // Display all bills
    public void showAllBills() {
        getAllBills().forEach(b -> {
            b.showBillDetails();
            System.out.println("---------------------");
        });
    }
}
