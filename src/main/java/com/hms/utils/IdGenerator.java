package com.hms.utils;

import com.hms.services.AdminService;
import com.hms.services.DoctorService;
import com.hms.services.PatientService;
import com.hms.services.ReceptionistService;

public abstract class IdGenerator {
    public static String generateStaffId(String rolePrefix) {
        // rolePrefix -> "doc", "ad", "rec"
        int entryNo;
        if (rolePrefix == "doc") {
            entryNo = DoctorService.getNextNumber();
        } else if (rolePrefix == "rec") {
            entryNo = ReceptionistService.getNextNumber();
        } else {
            entryNo = AdminService.getNextNumber();
        }
        return rolePrefix + entryNo;
    }

    public static String generateDoctorId(String specialization) {
        String specPart = specialization.length() >= 3
                ? specialization.substring(0, 3).toLowerCase()
                : specialization.toLowerCase();
        return "doc" + specPart + DoctorService.getNextNumber();
    }

    public static String generateAdminId(int ageAtJoining) {
        return "ad" + ageAtJoining + AdminService.getNextNumber();
    }

    public static String generateReceptionistId(int ageAtJoining) {
        return "rec" + ageAtJoining + ReceptionistService.getNextNumber();
    }

    public static String generatePatientId() {
        return "pat" + "1000" + PatientService.getNextNumber();
    }

    public static String generateUsername(String id) {
        return id + "un";
    }
}
