package com.hms.entities;

import java.time.LocalDate;
import com.hms.interfaces.Billable;

public class Billing implements Billable {
    private int billId;
    private Patient patient;
    private double amount;
    private LocalDate billDate;
    private boolean paid;

    public Billing(int billId, Patient patient, double amount, LocalDate billDate) {
        this.billId = billId;
        this.patient = patient;
        this.amount = amount;
        this.billDate = billDate;
        this.paid = false;
    }

    // Getters & Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public boolean isPaid() {
        return paid;
    }

    // Implement Billable methods
    @Override
    public double calculateBill() {
        // In a simple version, return current amount
        return amount;
    }

    @Override
    public void payBill(double amountPaid) {
        if (amountPaid >= amount) {
            paid = true;
            System.out.println("Bill paid in full. Thank you!");
        } else {
            amount -= amountPaid;
            System.out.println("Partial payment received. Remaining: $" + amount);
        }
    }

    @Override
    public void showBillDetails() {
        System.out.println("Bill ID: " + billId);
        System.out.println("Patient: " + patient.getFullName());
        System.out.println("Amount: $" + amount);
        System.out.println("Date: " + billDate);
        System.out.println("Status: " + (paid ? "Paid" : "Unpaid"));
    }
}
