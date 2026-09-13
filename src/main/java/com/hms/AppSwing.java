package com.hms;

import com.hms.entities.*;
import com.hms.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.hms.services.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class AppSwing {

    // ===== SERVICES =====
    static AdminService adminService = new AdminService();
    static DoctorService doctorService = new DoctorService();
    static ReceptionistService recService = new ReceptionistService();
    static PatientService patientService = new PatientService();
    static AppointmentService apptService = new AppointmentService();
    static BillingService billService = new BillingService();

    public static void main(String[] args) {
        seedSampleUsers();
        SwingUtilities.invokeLater(AppSwing::showLoginScreen);
    }

    // ===== LOGIN SCREEN =====
    private static void showLoginScreen() {
        JFrame frame = new JFrame("Hospital Management System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = { "Admin", "Doctor", "Receptionist" };
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");

        frame.add(roleLabel);
        frame.add(roleCombo);
        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(new JLabel());
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String role = (String) roleCombo.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            switch (role) {
                case "Admin" -> {
                    Optional<Admin> adminOpt = adminService.authenticate(username, password);
                    if (adminOpt.isPresent()) {
                        frame.dispose();
                        showAdminDashboard(adminOpt.get());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid credentials");
                    }
                }
                case "Doctor" -> {
                    Optional<Doctor> docOpt = doctorService.authenticate(username, password);
                    if (docOpt.isPresent()) {
                        frame.dispose();
                        showDoctorDashboard(docOpt.get());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid credentials");
                    }
                }
                case "Receptionist" -> {
                    Optional<Receptionist> recOpt = recService.authenticate(username, password);
                    if (recOpt.isPresent()) {
                        frame.dispose();
                        showReceptionistDashboard(recOpt.get());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid credentials");
                    }
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ===== ADMIN DASHBOARD =====
    private static void showAdminDashboard(Admin admin) {
        JFrame frame = new JFrame("Admin Dashboard - " + admin.getFullName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(6, 1, 10, 10));

        JButton addDocBtn = new JButton("Add Doctor");
        JButton addRecBtn = new JButton("Add Receptionist");
        JButton viewDocsBtn = new JButton("View Doctors");
        JButton viewRecsBtn = new JButton("View Receptionists");
        JButton viewPatientsBtn = new JButton("View Patients");
        JButton logoutBtn = new JButton("Logout");

        frame.add(addDocBtn);
        frame.add(addRecBtn);
        frame.add(viewDocsBtn);
        frame.add(viewRecsBtn);
        frame.add(viewPatientsBtn);
        frame.add(logoutBtn);

        addDocBtn.addActionListener(e -> addDoctorDialog(frame));
        addRecBtn.addActionListener(e -> addReceptionistDialog(frame));
        viewDocsBtn.addActionListener(e -> showTableDialog(frame, "Doctors", doctorService.getAllDoctors()));
        viewRecsBtn.addActionListener(e -> showTableDialog(frame, "Receptionists", recService.getAllReceptionists()));
        viewPatientsBtn.addActionListener(e -> showTableDialog(frame, "Patients", patientService.getAllPatients()));
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ===== DOCTOR DASHBOARD =====
    private static void showDoctorDashboard(Doctor doc) {
        JFrame frame = new JFrame("Doctor Dashboard - " + doc.getFullName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(5, 1, 10, 10));

        JButton viewApptsBtn = new JButton("View My Appointments");
        JButton viewPatientsBtn = new JButton("View My Patients");
        JButton viewPatientByIdBtn = new JButton("View Patient by Id");
        JButton markDoneBtn = new JButton("Mark Appointment Completed");
        JButton logoutBtn = new JButton("Logout");

        frame.add(viewApptsBtn);
        frame.add(viewPatientsBtn);
        frame.add(viewPatientByIdBtn);
        frame.add(markDoneBtn);
        frame.add(logoutBtn);

        viewApptsBtn.addActionListener(
                e -> showTableDialog(frame, "My Appointments", apptService.getAppointmentsByDoctor(doc.getDoctorId())));

        viewPatientsBtn.addActionListener(e -> {
            List<Patient> patients = apptService.getAppointmentsByDoctor(doc.getDoctorId())
                    .stream().map(Appointment::getPatient).distinct().toList();
            showTableDialog(frame, "My Patients", patients);
        });

        viewPatientByIdBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Patient Id:");
            if (id != null && !id.isEmpty()) {
                Optional<Patient> patOpt = patientService.getPatientById(id);
                if (patOpt.isPresent()) {
                    Patient pat = patOpt.get();
                    JOptionPane.showMessageDialog(frame, pat.showInfoString());
                } else {
                    JOptionPane.showMessageDialog(frame, "Patient not found");
                }
            }
        });

        markDoneBtn.addActionListener(e -> {
            List<Appointment> appts = apptService.getAppointmentsByDoctorSortedByTime(doc.getDoctorId());
            if (appts.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No appointments.");
                return;
            }
            String[] options = appts.stream()
                    .map(a -> a.getDateTime() + " | " + (a.getPatient() != null ? a.getPatient().getFullName() : "N/A"))
                    .toArray(String[]::new);
            String choice = (String) JOptionPane.showInputDialog(frame, "Select appointment:",
                    "Mark Completed", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice != null) {
                int idx = java.util.Arrays.asList(options).indexOf(choice);
                apptService.markAppointmentCompleted(appts.get(idx).getAppointmentId());
                JOptionPane.showMessageDialog(frame, "Appointment marked as completed");
            }
        });

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ===== RECEPTIONIST DASHBOARD =====
    private static void showReceptionistDashboard(Receptionist rec) {
        JFrame frame = new JFrame("Receptionist Dashboard - " + rec.getFullName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(6, 1, 10, 10));

        JButton addPatientBtn = new JButton("Add Patient");
        JButton scheduleApptBtn = new JButton("Schedule Appointment");
        JButton generateBillBtn = new JButton("Generate Bill");
        JButton viewApptsBtn = new JButton("View Appointments");
        JButton viewPatientsBtn = new JButton("View Patients");
        JButton logoutBtn = new JButton("Logout");

        frame.add(addPatientBtn);
        frame.add(scheduleApptBtn);
        frame.add(generateBillBtn);
        frame.add(viewApptsBtn);
        frame.add(viewPatientsBtn);
        frame.add(logoutBtn);

        addPatientBtn.addActionListener(e -> addPatientDialog(frame));
        scheduleApptBtn.addActionListener(e -> scheduleAppointmentDialog(frame));
        generateBillBtn.addActionListener(e -> generateBillDialog(frame));
        viewApptsBtn
                .addActionListener(e -> showTableDialog(frame, "All Appointments", apptService.getAllAppointments()));
        viewPatientsBtn.addActionListener(e -> showTableDialog(frame, "All Patients", patientService.getAllPatients()));

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // ===== DIALOGS & UTILITY METHODS =====

    private static void addDoctorDialog(Component parent) {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField specField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField feeField = new JTextField();
        JTextField salaryField = new JTextField();

        Object[] fields = {
                "Full Name:", nameField,
                "Age:", ageField,
                "Gender:", genderField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Specialization:", specField,
                "Department:", deptField,
                "Consultation Fee:", feeField,
                "Salary:", salaryField
        };

        int result = JOptionPane.showConfirmDialog(parent, fields, "Add Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String staffId = IdGenerator.generateStaffId("doc");
                String doctorId = IdGenerator.generateDoctorId(specField.getText().trim());
                String username = IdGenerator.generateUsername(doctorId);

                Doctor doc = new Doctor(
                        nameField.getText().trim(),
                        Integer.parseInt(ageField.getText().trim()),
                        genderField.getText().trim(),
                        phoneField.getText().trim(),
                        addressField.getText().trim(),
                        staffId,
                        username,
                        username,
                        Double.parseDouble(salaryField.getText().trim()),
                        doctorId,
                        specField.getText().trim(),
                        deptField.getText().trim(),
                        true,
                        Double.parseDouble(feeField.getText().trim()));

                if (doctorService.addDoctor(doc))
                    JOptionPane.showMessageDialog(parent, "Doctor added! Id: " + doctorId + ", Username: " + username);
                else
                    JOptionPane.showMessageDialog(parent, "Failed to add doctor.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Invalid input: " + ex.getMessage());
            }
        }
    }

    private static void addReceptionistDialog(Component parent) {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField salaryField = new JTextField();

        Object[] fields = {
                "Full Name:", nameField,
                "Age:", ageField,
                "Gender:", genderField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Salary:", salaryField
        };

        int result = JOptionPane.showConfirmDialog(parent, fields, "Add Receptionist", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String staffId = IdGenerator.generateStaffId("rec");
                String receptionistId = IdGenerator.generateReceptionistId(Integer.parseInt(ageField.getText().trim()));
                String username = IdGenerator.generateUsername(receptionistId);

                Receptionist rec = new Receptionist(
                        nameField.getText().trim(),
                        Integer.parseInt(ageField.getText().trim()),
                        genderField.getText().trim(),
                        phoneField.getText().trim(),
                        addressField.getText().trim(),
                        staffId,
                        username,
                        username,
                        Double.parseDouble(salaryField.getText().trim()),
                        "Morning",
                        "Desk1",
                        receptionistId);

                if (recService.addReceptionist(rec))
                    JOptionPane.showMessageDialog(parent,
                            "Receptionist added! Id: " + receptionistId + ", Username: " + username);
                else
                    JOptionPane.showMessageDialog(parent, "Failed to add receptionist.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Invalid input: " + ex.getMessage());
            }
        }
    }

    private static void addPatientDialog(Component parent) {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField conditionField = new JTextField();
        JTextField feeField = new JTextField();
        JCheckBox admitCheck = new JCheckBox("Admit patient");

        Object[] fields = {
                "Full Name:", nameField,
                "Age:", ageField,
                "Gender:", genderField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Condition:", conditionField,
                "Consultation Fee:", feeField,
                admitCheck
        };

        int result = JOptionPane.showConfirmDialog(parent, fields, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String patientId = IdGenerator.generatePatientId();
                boolean admitChoice = admitCheck.isSelected();
                String admitDate = null;
                if (admitChoice) {
                    admitDate = JOptionPane.showInputDialog(parent, "Enter Admit Date (yyyy-mm-dd):");
                }

                Patient pat = new Patient(
                        nameField.getText().trim(),
                        Integer.parseInt(ageField.getText().trim()),
                        genderField.getText().trim(),
                        phoneField.getText().trim(),
                        addressField.getText().trim(),
                        patientId,
                        conditionField.getText().trim(),
                        null,
                        admitChoice,
                        admitDate != null ? admitDate : "",
                        Double.parseDouble(feeField.getText().trim()));

                if (patientService.addPatient(pat))
                    JOptionPane.showMessageDialog(parent, "Patient added! Id: " + patientId);
                else
                    JOptionPane.showMessageDialog(parent, "Failed to add patient.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Invalid input: " + ex.getMessage());
            }
        }
    }

    private static void scheduleAppointmentDialog(Component parent) {
        List<Patient> patients = patientService.getAllPatients();
        List<Doctor> doctors = doctorService.getAllDoctors();

        if (patients.isEmpty() || doctors.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Need at least 1 patient & 1 doctor.");
            return;
        }

        String[] patOptions = patients.stream().map(Patient::getFullName).toArray(String[]::new);
        String[] docOptions = doctors.stream().map(Doctor::getFullName).toArray(String[]::new);

        String patChoice = (String) JOptionPane.showInputDialog(parent, "Select Patient:", "Schedule Appointment",
                JOptionPane.PLAIN_MESSAGE, null, patOptions, patOptions[0]);
        String docChoice = (String) JOptionPane.showInputDialog(parent, "Select Doctor:", "Schedule Appointment",
                JOptionPane.PLAIN_MESSAGE, null, docOptions, docOptions[0]);

        if (patChoice != null && docChoice != null) {
            int pIndex = java.util.Arrays.asList(patOptions).indexOf(patChoice);
            int dIndex = java.util.Arrays.asList(docOptions).indexOf(docChoice);

            String dateTimeStr = JOptionPane.showInputDialog(parent, "Enter DateTime (YYYY-MM-DDTHH:MM):");
            try {
                LocalDateTime dt = LocalDateTime.parse(dateTimeStr);
                Appointment appt = apptService.scheduleAppointment(patients.get(pIndex), doctors.get(dIndex), dt);
                if (appt != null) {
                    JOptionPane.showMessageDialog(parent,
                            "Appointment scheduled for " + appt.getPatient().getFullName() +
                                    " with " + appt.getDoctor().getFullName() + " on " + appt.getDateTime());
                } else
                    JOptionPane.showMessageDialog(parent, "Failed to schedule appointment.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Invalid date-time format");
            }
        }
    }

    private static void generateBillDialog(Component parent) {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No patients.");
            return;
        }

        String[] patOptions = patients.stream().map(Patient::getFullName).toArray(String[]::new);
        String patChoice = (String) JOptionPane.showInputDialog(parent, "Select Patient:", "Generate Bill",
                JOptionPane.PLAIN_MESSAGE, null, patOptions, patOptions[0]);

        if (patChoice != null) {
            int pIndex = java.util.Arrays.asList(patOptions).indexOf(patChoice);
            Patient pat = patients.get(pIndex);
            double amount = apptService.getAppointmentsByPatient(pat.getPatientId())
                    .stream().mapToDouble(a -> a.getDoctor().getConsultationFee()).sum();

            Billing bill = billService.createBill(pat, amount);
            if (bill != null) {
                int pay = JOptionPane.showConfirmDialog(parent, "Pay full amount now?");
                if (pay == JOptionPane.YES_OPTION) {
                    bill.payBill(amount);
                }
                JOptionPane.showMessageDialog(parent, "Bill saved.");
            } else
                JOptionPane.showMessageDialog(parent, "Failed to save bill.");
        }
    }

    // Generic table display
    private static void showTableDialog(Component parent, String title, List<?> list) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No data.");
            return;
        }

        String[] columnNames = Arrays.stream(list.get(0).getClass().getDeclaredFields())
                .map(f -> f.getName()).toArray(String[]::new);

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Object obj : list) {
            try {
                Object[] row = Arrays.stream(obj.getClass().getDeclaredFields())
                        .map(f -> {
                            f.setAccessible(true);
                            try {
                                return f.get(obj);
                            } catch (Exception e) {
                                return "";
                            }
                        }).toArray();
                model.addRow(row);
            } catch (Exception ignored) {
            }
        }

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(600, 300));
        JOptionPane.showMessageDialog(parent, scroll, title, JOptionPane.PLAIN_MESSAGE);
    }

    private static void seedSampleUsers() {
        adminService.addAdmin(new Admin("Super Admin", 40, "M", "111", "",
                "ad1", "ad401un", "ad401un", 80000, "ad401", 1, "Management"));
        doctorService.addDoctor(new Doctor("Dr. Strange", 45, "M", "222", "",
                "doc1", "docCar1un", "docCar1un", 50000, "docCar1", "Cardiology", "General", true, 1000));
        recService.addReceptionist(new Receptionist("Alice", 28, "F", "333", "",
                "rec1", "rec281un", "rec281un", 30000, "Morning", "Desk1", "rec281"));
    }
}
