package com.hms.entities;

public class Patient extends Person {
    private String patientId;
    private String illness;
    private String doctorId;
    private boolean admitted;
    private String admitDate;
    private double billingAmt;

    public Patient(String name, int age, String gender, String phoneNumber, String address, String patientId,
            String illness, String doctorId, boolean admitted, String admitDate, double billingAmt) {
        super(name, age, gender, phoneNumber, address);
        this.patientId = patientId;
        this.illness = illness;
        this.doctorId = doctorId;
        this.admitted = admitted;
        this.admitDate = admitDate;
        this.billingAmt = billingAmt;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public boolean isAdmitted() {
        return admitted;
    }

    public void setAdmitted(boolean admitted) {
        this.admitted = admitted;
    }

    public String getAdmitDate() {
        return admitDate;
    }

    public void setAdmitDate(String admitDate) {
        this.admitDate = admitDate;
    }

    public double getBillingAmt() {
        return billingAmt;
    }

    public void setBillingAmt(double billingAmt) {
        this.billingAmt = billingAmt;
    }

    @Override
    public void showInfo() {
        super.showInfo(); // prints Person details
        System.out.println("Patient ID: " + patientId);
        System.out.println("Illness: " + illness);
        System.out.println("Admission Date: " + admitDate);
        System.out.println("Doctor Assigned: " + doctorId);
        System.out.println("Admitted: " + (admitted ? "Yes" : "No"));
        System.out.println("Billing Amount: " + billingAmt);
        System.out.println("======================================");
    }

    public String showInfoString() {
        return "Patient ID: " + patientId +
                "\nName: " + getFullName() +
                "\nAge: " + getAge() +
                "\nGender: " + getGender() +
                "\nPhone: " + getPhoneNumber() +
                "\nAddress: " + getAddress() +
                "\nCondition: " + illness +
                "\nAdmitted: " + admitted +
                (admitDate != null ? "\nAdmit Date: " + admitDate : "");
    }

}