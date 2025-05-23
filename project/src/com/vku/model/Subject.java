package com.vku.model;

/**
 * Represents a subject or course in the system.
 */
public class Subject {
    private String id;
    private String name;
    private int credits;
    private Teacher teacher;
    
    public Subject(String id, String name, int credits, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.teacher = teacher;
        
        // Add this subject to the teacher's list
        teacher.addSubject(this);
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
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subject subject = (Subject) obj;
        return id.equals(subject.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}