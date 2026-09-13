package com.hms.dao;

import com.hms.DBConnection;
import com.hms.entities.Appointment;
import com.hms.entities.Doctor;
import com.hms.entities.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDAO {

    public boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, date_time) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getPatient().getPatientId());
            ps.setString(2, a.getDoctor().getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(a.getDateTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("⚠️ Failed to insert appointment: " + e.getMessage());
            return false;
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to fetch appointments: " + e.getMessage());
        }
        return appointments;
    }

    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("⚠️ Failed to delete appointment: " + e.getMessage());
            return false;
        }
    }

    /** ✅ Count all appointments */
    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM appointments";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next())
                return rs.getInt("total");

        } catch (SQLException e) {
            System.err.println("⚠️ Failed to count appointments: " + e.getMessage());
        }
        return 0;
    }

    /** ✅ Fetch appointment by ID */
    public Optional<Appointment> getAppointmentById(int id) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to fetch appointment: " + e.getMessage());
        }
        return Optional.empty();
    }

    /** ✅ All appointments for a given doctor */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? AND status='Scheduled'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to fetch appointments by doctor: " + e.getMessage());
        }
        return list;
    }

    /** ✅ All appointments for a given patient */
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to fetch appointments by patient: " + e.getMessage());
        }
        return list;
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        DoctorDAO doctorDAO = new DoctorDAO();
        PatientDAO patientDAO = new PatientDAO();
        String patientId = rs.getString("patient_id");
        Optional<Patient> patientOpt = patientDAO.getPatientById(patientId);
        Patient patient = patientOpt.orElse(null); // use null if patient not found

        String doctorId = rs.getString("doctor_id");
        Optional<Doctor> doctorOpt = doctorDAO.getById(doctorId);
        Doctor doctor = doctorOpt.orElse(null); // use null if doctor not found

        return new Appointment(
                rs.getInt("appointment_id"),
                patient,
                doctor,
                rs.getTimestamp("date_time").toLocalDateTime());
    }

    public List<Patient> getPatientsByDoctor(String doctorId) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM patients p " +
                "JOIN appointments a ON p.patient_id = a.patient_id " +
                "WHERE a.doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String admitDate = rs.getDate("admit_date") != null
                        ? rs.getDate("admit_date").toString()
                        : null;
                patients.add(new Patient(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("patient_id"),
                        rs.getString("illness"),
                        rs.getString("doctor_id"),
                        rs.getBoolean("admitted"),
                        admitDate,
                        rs.getDouble("billing_amt")));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch patients for doctor: " + e.getMessage());
        }
        return patients;
    }

    public List<Appointment> getAppointmentsByDoctorSortedByTime(String doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments " +
                "WHERE doctor_id = ? AND status = 'Scheduled' " +
                "ORDER BY date_time ASC"; // closest upcoming first

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                appointments.add(mapAppointment(rs)); // map patient & doctor objects
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch appointments: " + e.getMessage());
        }
        return appointments;
    }

    public boolean markAppointmentCompleted(int appointmentId) {
        String sql = "UPDATE appointments SET status = 'Completed' WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            int updatedRows = ps.executeUpdate();
            return updatedRows > 0; // true if at least one row updated
        } catch (SQLException e) {
            System.err.println("Failed to mark appointment completed: " + e.getMessage());
            return false;
        }
    }

}
