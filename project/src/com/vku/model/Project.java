package com.vku.model;

import java.util.List;

/**
 * Represents a project assigned to students.
 */
public class Project {
    private String id;
    private String name;
    private List<Student> students;
    private Teacher supervisor;
    private String deadline;
    private int progress;
    private double grade;
    
    public Project(String id, String name, List<Student> students, Teacher supervisor, String deadline) {
        this.id = id;
        this.name = name;
        this.students = students;
        this.supervisor = supervisor;
        this.deadline = deadline;
        this.progress = 0;
        this.grade = 0.0;
    }
    
    // Getters and setters
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
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
    public Teacher getSupervisor() {
        return supervisor;
    }
    
    public void setSupervisor(Teacher supervisor) {
        this.supervisor = supervisor;
    }
    
    public String getDeadline() {
        return deadline;
    }
    
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public void setProgress(int progress) {
        this.progress = progress;
    }
    
    public double getGrade() {
        return grade;
    }
    
    public void setGrade(double grade) {
        this.grade = grade;
    }
}