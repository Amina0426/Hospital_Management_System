package com.hms;

import com.hms.entities.*;
import com.hms.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import com.hms.services.*;

public class App {
    static Scanner sc = new Scanner(System.in);

    // ===== SERVICES =====
    static AdminService adminService = new AdminService();
    static DoctorService doctorService = new DoctorService();
    static ReceptionistService recService = new ReceptionistService();
    static PatientService patientService = new PatientService();
    static AppointmentService apptService = new AppointmentService();
    static BillingService billService = new BillingService();

    public static void main(String[] args) {
        seedSampleUsers(); // optional test accounts
        mainMenu();
        sc.close();
    }

    // ================ MAIN MENU ================
    private static void mainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== HOSPITAL MANAGEMENT SYSTEM ===");
            System.out.println("1. Admin Login");
            System.out.println("2. Doctor Login");
            System.out.println("3. Receptionist Login");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String input = sc.nextLine();
            switch (input) {
                case "1" -> adminLogin();
                case "2" -> doctorLogin();
                case "3" -> receptionistLogin();
                case "4" -> exit = true;
                default -> System.out.println("Invalid option!");
            }
        }
        System.out.println("Goodbye!");
    }

    // ================ LOGIN HANDLERS ================
    private static void adminLogin() {
        System.out.print("Admin username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        Optional<Admin> adminOpt = adminService.authenticate(user, pass);
        adminOpt.ifPresentOrElse(App::adminDashboard, () -> System.out.println("Invalid credentials."));
    }

    private static void doctorLogin() {
        System.out.print("Doctor username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        Optional<Doctor> docOpt = doctorService.authenticate(user, pass);
        docOpt.ifPresentOrElse(App::doctorDashboard, () -> System.out.println("Invalid credentials."));
    }

    private static void receptionistLogin() {
        System.out.print("Receptionist username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        Optional<Receptionist> recOpt = recService.authenticate(user, pass);
        recOpt.ifPresentOrElse(App::receptionistDashboard, () -> System.out.println("Invalid credentials."));
    }

    // ================ DASHBOARDS ================
    private static void adminDashboard(Admin admin) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. Add Doctor");
            System.out.println("2. Add Receptionist");
            System.out.println("3. View Doctors");
            System.out.println("4. View Receptionists");
            System.out.println("5. View Patients");
            System.out.println("6. Logout");
            System.out.print("Choice: ");
            switch (sc.nextLine()) {
                case "1" -> addDoctor();
                case "2" -> addReceptionist();
                case "3" -> doctorService.getAllDoctors().forEach(Doctor::showInfo);
                case "4" -> recService.getAllReceptionists().forEach(Receptionist::showInfo);
                case "5" -> patientService.getAllPatients().forEach(Patient::showInfo);
                case "6" -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void doctorDashboard(Doctor doc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== DOCTOR DASHBOARD ===");
            System.out.println("1. View My Appointments");
            System.out.println("2. View My Patients");
            System.out.println("3. View Patient by Id");
            System.out.println("4. Mark Appointment as Completed");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            switch (sc.nextLine()) {
                case "1" -> apptService.getAppointmentsByDoctor(doc.getDoctorId())
                        .forEach(Appointment::showInfo);
                case "2" -> apptService.getAppointmentsByDoctor(doc.getDoctorId())
                        .stream().map(Appointment::getPatient).distinct()
                        .forEach(Patient::showInfo);
                case "3" -> viewPatientById(doc.getDoctorId());
                case "4" -> markAsDone(doc.getDoctorId());
                case "5" -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void receptionistDashboard(Receptionist rec) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== RECEPTIONIST DASHBOARD ===");
            System.out.println("1. Add Patient");
            System.out.println("2. Schedule Appointment");
            System.out.println("3. Generate Bill");
            System.out.println("4. View Appointments");
            System.out.println("5. View Patients");
            System.out.println("6. Logout");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine().trim());
            switch (choice) {
                case 1 -> addPatient();
                case 2 -> scheduleAppointment();
                case 3 -> generateBill();
                case 4 -> apptService.getAllAppointments().forEach(Appointment::showInfo);
                case 5 -> patientService.getAllPatients()
                        .forEach(p -> p.showInfo());
                case 6 -> back = true;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ================ CRUD METHODS ================
    private static void addDoctor() {
        System.out.println("--- Add Doctor ---");
        System.out.print("Full Name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Gender: ");
        String gender = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();
        System.out.print("Specialization: ");
        String specialization = sc.nextLine();
        System.out.print("Department: ");
        String dept = sc.nextLine();
        System.out.print("Fee: ");
        double fee = Double.parseDouble(sc.nextLine());
        System.out.print("Salary assigned: ");
        double salary = Double.parseDouble(sc.nextLine());
        String staffId = IdGenerator.generateStaffId("doc");
        String doctorId = IdGenerator.generateDoctorId(specialization);
        String username = IdGenerator.generateUsername(doctorId);

        Doctor doc = new Doctor(name, age, gender, phone, address, staffId, username, username, salary, doctorId,
                specialization, dept, true, fee);

        if (doctorService.addDoctor(doc)) {
            System.out.println("Doctor added! Generated Ids: " + staffId + ", " + doctorId + ", username: " + username);
        } else
            System.out.println("Failed to add doctor.");
    }

    private static void addReceptionist() {
        System.out.println("--- Add Receptionist ---");
        System.out.print("Full Name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Gender: ");
        String gender = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();
        System.out.print("Salary assigned: ");
        double salary = Double.parseDouble(sc.nextLine());
        String staffId = IdGenerator.generateStaffId("rec");
        String receptionistId = IdGenerator.generateReceptionistId(age);
        String username = IdGenerator.generateUsername(receptionistId);

        Receptionist rec = new Receptionist(name, age, gender, phone, address,
                staffId, username, username, salary, "Morning", "Desk1", receptionistId);

        if (recService.addReceptionist(rec)) {
            System.out.println("Receptionist added! Generated Ids: " + staffId + ", " + receptionistId + ", username: "
                    + username);
        } else
            System.out.println("Failed to add receptionist.");
    }

    private static void addPatient() {
        System.out.println("--- Add Patient ---");
        System.out.print("Full Name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Gender: ");
        String gender = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();
        System.out.print("Condition: ");
        String cond = sc.nextLine();
        System.out.print("Consultation Fee: ");
        double fee = Double.parseDouble(sc.nextLine());
        System.out.print("Admit patient? (true/false): ");
        String admitInput = sc.nextLine().trim();
        boolean admitChoice = Boolean.parseBoolean(admitInput);
        String patientId = IdGenerator.generatePatientId();
        Patient pat = new Patient(name, age, gender, phone, address, patientId, cond, null, admitChoice, "", fee);
        if (admitChoice) {
            System.out.print("Enter Admit Date (yyyy-mm-dd): ");
            pat.setAdmitDate(sc.nextLine().trim());
        } else {
            pat.setAdmitDate(null);
        }

        if (patientService.addPatient(pat)) {
            System.out.println("Patient added! PatientId: " + patientId);
        } else
            System.out.println("Failed to add patient.");
    }

    private static void scheduleAppointment() {
        List<Patient> patients = patientService.getAllPatients();
        List<Doctor> doctors = doctorService.getAllDoctors();

        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("Need at least 1 patient & 1 doctor.");
            return;
        }

        System.out.println("Select Patient:");
        for (int i = 0; i < patients.size(); i++)
            System.out.println((i + 1) + ". " + patients.get(i).getFullName());
        int pIndex = Integer.parseInt(sc.nextLine()) - 1;

        System.out.println("Select Doctor:");
        for (int i = 0; i < doctors.size(); i++)
            System.out.println((i + 1) + ". " + doctors.get(i).getFullName());
        int dIndex = Integer.parseInt(sc.nextLine()) - 1;

        System.out.print("DateTime (YYYY-MM-DDTHH:MM): ");
        LocalDateTime dt = LocalDateTime.parse(sc.nextLine());

        Appointment appt = apptService.scheduleAppointment(patients.get(pIndex), doctors.get(dIndex), dt);
        if (appt != null) {
            System.out.println("Appointment scheduled for " + appt.getPatient().getFullName() +
                    " with " + appt.getDoctor().getFullName() + " on " + appt.getDateTime());
        } else {
            System.out.println("Failed to schedule appointment.");
        }
    }

    private static void generateBill() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients.");
            return;
        }

        System.out.println("Select Patient:");
        for (int i = 0; i < patients.size(); i++)
            System.out.println((i + 1) + ". " + patients.get(i).getFullName());
        int pIndex = Integer.parseInt(sc.nextLine()) - 1;

        Patient pat = patients.get(pIndex);
        double amount = apptService.getAppointmentsByPatient(pat.getPatientId())
                .stream().mapToDouble(a -> a.getDoctor().getConsultationFee()).sum();

        Billing bill = billService.createBill(patients.get(pIndex), amount);
        if (bill != null) {
            System.out.print("Pay full amount? (yes/no): ");
            if (sc.nextLine().equalsIgnoreCase("yes")) {
                bill.payBill(amount);
            }
            System.out.println("Bill saved.");
        } else {
            System.out.println("Failed to save bill.");
        }

    }

    private static void viewPatientById(String doctorId) {
        List<Patient> myPatients = apptService.getPatientsByDoctor(doctorId);

        if (myPatients.isEmpty()) {
            System.out.println("No patients assigned yet.");
            return;
        }

        System.out.println("Select Patient:");
        for (int i = 0; i < myPatients.size(); i++) {
            System.out.println((i + 1) + ". " + myPatients.get(i).getFullName());
        }

        int choice = Integer.parseInt(sc.nextLine().trim()) - 1;
        if (choice < 0 || choice >= myPatients.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Patient selectedPatient = myPatients.get(choice);
        selectedPatient.showInfo(); // shows only info of this patient

    }

    private static void markAsDone(String doctorId) {
        List<Appointment> myAppointments = apptService.getAppointmentsByDoctorSortedByTime(doctorId);

        if (myAppointments.isEmpty()) {
            System.out.println("No scheduled appointments.");
            return;
        }

        System.out.println("Select an appointment to mark as done:");
        for (int i = 0; i < myAppointments.size(); i++) {
            Appointment a = myAppointments.get(i);
            System.out.println((i + 1) + ". " +
                    a.getDateTime() + " | Patient: " +
                    (a.getPatient() != null ? a.getPatient().getFullName() : "N/A"));
        }
        int choice = Integer.parseInt(sc.nextLine().trim()) - 1;
        if (choice < 0 || choice >= myAppointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Appointment selectedAppt = myAppointments.get(choice);
        boolean success = apptService.markAppointmentCompleted(selectedAppt.getAppointmentId());
        if (success) {
            System.out.println("Appointment marked as completed.");
        } else {
            System.out.println("Failed to mark appointment as completed.");
        }

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
