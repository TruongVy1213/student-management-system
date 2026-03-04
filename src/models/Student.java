package models;

import java.time.LocalDate;
import java.time.Period;

public class Student {
    private String studentId;
    private String name;
    private LocalDate dateOfBirth;
    private String major;
    private double gpa;
    private String className;

    public Student(String studentId, String name, LocalDate dateOfBirth, 
                   String major, double gpa, String className) {
        this.studentId = studentId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.major = major;
        this.gpa = gpa;
        this.className = className;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getMajor() { return major; }
    public double getGpa() { return gpa; }
    public String getClassName() { return className; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setMajor(String major) { this.major = major; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    public void setClassName(String className) { this.className = className; }

    // Tính tuổi
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-25s | %s | %s | %6.2f | %-15s | Tuổi: %d |",
                studentId, name, dateOfBirth, major, gpa, className, getAge());
    }
}