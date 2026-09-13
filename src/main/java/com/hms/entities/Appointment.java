package com.hms.entities;

import java.time.LocalDateTime;

import com.hms.interfaces.Schedulable;

public class Appointment implements Schedulable {
    private int appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private String status;

    public Appointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime dateTime) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = dateTime;
        this.status = "Scheduled";
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getDateTime() {
        return appointmentDateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.appointmentDateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void schedule() {
        this.status = "Scheduled";
        System.out.println("Appointment scheduled for " + patient.getFullName() + " with Dr. " + doctor.getFullName()
                + " at " + appointmentDateTime);
    }

    @Override
    public void cancel() {
        this.status = "Cancelled";
        System.out.println("Appointment cancelled for " + patient.getFullName());
    }

    @Override
    public void reschedule(String newDateTime) {
        this.appointmentDateTime = LocalDateTime.parse(newDateTime); // expects ISO format
        this.status = "Rescheduled";
        System.out.println("Appointment rescheduled for " + patient.getFullName() + " to " + appointmentDateTime);
    }

    public void showInfo() {
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Patient: " + patient.getFullName());
        System.out.println("Doctor: " + doctor.getFullName());
        System.out.println("Date & Time: " + appointmentDateTime);
        System.out.println("Status: " + status);
        System.out.println("==================================");
    }
}
