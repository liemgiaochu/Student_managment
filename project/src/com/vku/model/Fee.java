package com.vku.model;

/**
 * Represents a fee record for a student.
 */
public class Fee {
    private Student student;
    private double amount;
    private boolean paid;
    
    public Fee(Student student, double amount, boolean paid) {
        this.student = student;
        this.amount = amount;
        this.paid = paid;
    }
    
    // Getters and setters
    public Student getStudent() {
        return student;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public boolean isPaid() {
        return paid;
    }
    
    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}