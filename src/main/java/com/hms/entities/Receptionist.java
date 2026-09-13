package com.hms.entities;

public class Receptionist extends Staff {
    private String shiftTiming;
    private String deskNumber;
    private String receptionistId;

    public Receptionist(String name, int age, String gender, String phoneNumber, String address,
            String staffId, String userName, String password, double salary,
            String shiftTiming, String deskNumber, String receptionistId) {
        super(name, age, gender, phoneNumber, address, staffId, "Receptionist", userName, password, salary);
        this.shiftTiming = shiftTiming;
        this.deskNumber = deskNumber;
        this.receptionistId = receptionistId;
    }

    public String getShiftTiming() {
        return shiftTiming;
    }

    public void setShiftTiming(String shiftTiming) {
        this.shiftTiming = shiftTiming;
    }

    public String getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(String deskNumber) {
        this.deskNumber = deskNumber;
    }

    public String getReceptionistId() {
        return receptionistId;
    }

    public void setReceptionistId(String receptionistId) {
        this.receptionistId = receptionistId;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Receptionist ID: " + receptionistId);
        System.out.println("Desk Number: " + deskNumber);
        System.out.println("Shift Timing: " + shiftTiming);
        System.out.println("======================================");
    }
}