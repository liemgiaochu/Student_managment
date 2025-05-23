package com.vku.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a teacher in the system.
 */
public class Teacher extends User {
    private List<Subject> subjects;
    
    public Teacher(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.subjects = new ArrayList<>();
    }
    
    /**
     * Adds a subject to the teacher's list of taught subjects.
     * 
     * @param subject the subject to add
     */
    public void addSubject(Subject subject) {
        if (!subjects.contains(subject)) {
            subjects.add(subject);
        }
    }
    
    /**
     * Gets the list of subjects taught by the teacher.
     * 
     * @return the list of subjects
     */
    public List<Subject> getSubjects() {
        return subjects;
    }
}