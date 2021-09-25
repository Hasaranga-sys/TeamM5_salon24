package com.tech.saloonmanagment.Models;

public class EmployeeDetails {

    String id;
    String name;
    String age;
    String contact;
    String address;
    String time;
    String appointmentPrice;
    String salary;

    public EmployeeDetails() {
    }

    public EmployeeDetails(String id, String name, String age, String contact, String address, String time, String appointmentPrice, String salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.time = time;
        this.appointmentPrice = appointmentPrice;
        this.salary = salary;
    }

    public String getAppointmentPrice() {
        return appointmentPrice;
    }

    public void setAppointmentPrice(String appointmentPrice) {
        this.appointmentPrice = appointmentPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
