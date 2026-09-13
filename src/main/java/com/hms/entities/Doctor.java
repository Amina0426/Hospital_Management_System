package com.hms.entities;

public class Doctor extends Staff {
    private String doctorId;
    private String specialization;
    private String department;
    private boolean availability;
    private double consultationFee;

    public Doctor(String name, int age, String gender, String phoneNumber, String address,
            String staffId, String userName, String password, double salary,
            String doctorId, String specialization, String department, boolean availability,
            double consultationFee) {
        super(name, age, gender, phoneNumber, address, staffId, "Doctor", userName, password, salary);
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.department = department;
        this.availability = availability;
        this.consultationFee = consultationFee;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Specialization: " + specialization);
        System.out.println("Department: " + department);
        System.out.println("Available: " + (availability ? "Yes" : "No"));
        System.out.println("Consultation Fee: " + consultationFee);
        System.out.println("======================================");
    }
}