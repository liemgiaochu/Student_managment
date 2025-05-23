package com.vku.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student in the system.
 */
public class Student extends User {
    private String className;
    private String major;
    private String course; // Academic years (e.g., 2021-2025)
    private String dateOfBirth;
    private String gender;
    private String parentName;
    private String parentContact;
    private String address;
    private String phone;
    private List<Subject> subjects;
    private List<Grade> grades;
    
    public Student(String id, String name, String className, String major, String course, 
                  String email, String password) {
        super(id, name, email, password);
        this.className = className;
        this.major = major;
        this.course = course;
        this.subjects = new ArrayList<>();
        this.grades = new ArrayList<>();
    }
    
    /**
     * Calculates the GPA of the student based on a 10-point scale.
     * 
     * @return the GPA of the student
     */
    public double calculateGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        
        double totalPoints = 0.0;
        double totalCredits = 0.0;
        
        for (Grade grade : grades) {
            Subject subject = grade.getSubject();
            double finalScore = grade.getFinalScore();
            int credits = subject.getCredits();
            
            totalPoints += finalScore * credits;
            totalCredits += credits;
        }
        
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }
    
    /**
     * Calculates the GPA of the student based on a 4-point scale.
     * 
     * @return the GPA of the student
     */
    public double calculateGPA4Scale() {
        double gpa10 = calculateGPA();
        
        // Convert 10-scale to 4-scale
        if (gpa10 >= 8.5) return 4.0;
        if (gpa10 >= 8.0) return 3.7;
        if (gpa10 >= 7.5) return 3.3;
        if (gpa10 >= 7.0) return 3.0;
        if (gpa10 >= 6.5) return 2.7;
        if (gpa10 >= 6.0) return 2.3;
        if (gpa10 >= 5.5) return 2.0;
        if (gpa10 >= 5.0) return 1.7;
        if (gpa10 >= 4.0) return 1.0;
        return 0.0;
    }
    
    /**
     * Gets the rank of the student based on GPA.
     * 
     * @return the rank of the student (A, B, C, D, or F)
     */
    public String getRank() {
        double gpa = calculateGPA();
        
        if (gpa >= 8.5) return "A";
        if (gpa >= 7.0) return "B";
        if (gpa >= 5.5) return "C";
        if (gpa >= 4.0) return "D";
        return "F";
    }
    
    /**
     * Gets the total credits completed by the student.
     * 
     * @return the total credits
     */
    public int getTotalCredits() {
        int totalCredits = 0;
        for (Subject subject : subjects) {
            totalCredits += subject.getCredits();
        }
        return totalCredits;
    }
    
    // Add subject to student's list
    public void addSubject(Subject subject) {
        if (!subjects.contains(subject)) {
            subjects.add(subject);
        }
    }
    
    // Add grade for student
    public void addGrade(Grade grade) {
        grades.add(grade);
    }
    
    // Getters and setters
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getParentName() {
        return parentName;
    }
    
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    
    public String getParentContact() {
        return parentContact;
    }
    
    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<Subject> getSubjects() {
        return subjects;
    }
    
    public List<Grade> getGrades() {
        return grades;
    }
}