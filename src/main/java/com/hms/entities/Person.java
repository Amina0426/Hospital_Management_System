package com.hms.entities;

public abstract class Person {
    private String fullName;
    private int age;
    private String gender;
    private String phoneNumber;
    private String address;

    public Person(String name, int age, String gender, String phoneNumber, String address) {
        this.fullName = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String pn) {
        this.phoneNumber = pn;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String add) {
        this.address = add;
    }

    public void showInfo() {
        System.out.println("Name: " + fullName +
                "\nAge: " + age +
                "\nGender: " + gender +
                "\nPhone: " + phoneNumber +
                "\nAddress: " + address);
    }
}
