package com.example.ruleEngine.model;

public class User {
    private String id; // MongoDB ID
    private int age;
    private double salary;
    private String department;
    private int experience;

    public User(int age, double salary, String department, int experience) {
        this.age = age;
        this.salary = salary;
        this.department = department;
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}