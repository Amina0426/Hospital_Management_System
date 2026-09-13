package com.hms.entities;

public class Admin extends Staff {
    private String adminId;
    private int adminLevel;
    private String departmentAssigned;

    public Admin(String fullName, int age, String gender,
            String phoneNumber, String address,
            String staffId, String username, String password,
            double salary,
            String adminId, int adminLevel, String departmentAssigned) {
        super(fullName, age, gender, phoneNumber, address, staffId, "Admin", username, password, salary);
        this.adminId = adminId;
        this.adminLevel = adminLevel;
        this.departmentAssigned = departmentAssigned;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getDepartmentAssigned() {
        return departmentAssigned;
    }

    public void setDepartmentAssigned(String departmentAssigned) {
        this.departmentAssigned = departmentAssigned;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Admin ID: " + adminId);
        System.out.println("Admin Level: " + adminLevel);
        System.out.println("Department Assigned: " + departmentAssigned);
        System.out.println("======================================");
    }
}
