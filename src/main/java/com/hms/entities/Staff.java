package com.hms.entities;

public abstract class Staff extends Person {
    private String staffId;
    private String role;
    private String userName;
    private String password;
    private double salary;

    public Staff(String name, int age, String gender, String phoneNumber, String address,
            String staffId, String role, String userName, String password, double salary) {
        super(name, age, gender, phoneNumber, address);
        this.staffId = staffId;
        this.role = role;
        this.userName = userName;
        this.password = password;
        this.salary = salary;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Staff ID: " + staffId);
        System.out.println("Role: " + role);
        System.out.println("Salary: $" + salary);
    }
}
