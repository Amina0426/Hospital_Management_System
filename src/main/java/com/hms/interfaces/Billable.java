package com.hms.interfaces;

public interface Billable {
    double calculateBill();

    void payBill(double amount);

    void showBillDetails();
}
