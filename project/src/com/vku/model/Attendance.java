package com.vku.model;

import java.util.Date;

/**
 * Represents an attendance record for a student in a specific subject.
 */
public class Attendance {
    private Student student;
    private Subject subject;
    private Date date;
    private boolean present;
    
    public Attendance(Student student, Subject subject, Date date, boolean present) {
        this.student = student;
        this.subject = subject;
        this.date = date;
        this.present = present;
    }
    
    // Getters and setters
    public Student getStudent() {
        return student;
    }
    
    public Subject getSubject() {
        return subject;
    }
    
    public Date getDate() {
        return date;
    }
    
    public boolean isPresent() {
        return present;
    }
    
    public void setPresent(boolean present) {
        this.present = present;
    }
}