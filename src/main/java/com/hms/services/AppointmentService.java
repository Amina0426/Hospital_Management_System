package com.hms.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hms.dao.AppointmentDAO;
import com.hms.dao.PatientDAO;
import com.hms.entities.*;

public class AppointmentService {
    private final AppointmentDAO apptDAO = new AppointmentDAO();

    public Appointment scheduleAppointment(Patient p, Doctor d, LocalDateTime dateTime) {
        PatientDAO patientDAO = new PatientDAO();
        int id = apptDAO.count() + 1;
        Appointment appt = new Appointment(id, p, d, dateTime);

        if (apptDAO.addAppointment(appt)) {
            // Update the patient's doctor_id to reflect this assignment
            patientDAO.updateDoctorForPatient(p.getPatientId(), d.getDoctorId());
            // Also update the Patient object in memory
            p.setDoctorId(d.getDoctorId());
            return appt;
        }
        return null;
    }

    public boolean cancelAppointment(int appointmentId) {
        return apptDAO.deleteAppointment(appointmentId);
    }

    public List<Appointment> getAllAppointments() {
        return apptDAO.getAllAppointments();
    }

    public Optional<Appointment> getAppointmentById(int appointmentId) {
        return apptDAO.getAppointmentById(appointmentId);
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return apptDAO.getAppointmentsByDoctor(doctorId);
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return apptDAO.getAppointmentsByPatient(patientId);
    }

    public List<Patient> getPatientsByDoctor(String doctorId) {
        return apptDAO.getPatientsByDoctor(doctorId);
    }

    public List<Appointment> getAppointmentsByDoctorSortedByTime(String doctorId) {
        return apptDAO.getAppointmentsByDoctorSortedByTime(doctorId);
    }

    public boolean markAppointmentCompleted(int id) {
        return apptDAO.markAppointmentCompleted(id);
    }
}
