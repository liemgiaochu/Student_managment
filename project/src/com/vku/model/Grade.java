package com.vku.model;

/**
 * Represents a student's grade for a specific subject.
 */
public class Grade {
    private Student student;
    private Subject subject;
    private double assignmentScore;
    private double midtermScore;
    private double attendanceScore;
    private double finalScore;
    
    public Grade(Student student, Subject subject) {
        this.student = student;
        this.subject = subject;
    }
    
    /**
     * Calculates the average score based on the weighted components:
     * - Assignment: 20%
     * - Midterm: 20%
     * - Attendance: 10%
     * - Final: 50%
     * 
     * @return the calculated average score
     */
    public double calculateAverage() {
        return (assignmentScore * 0.2) + (midtermScore * 0.2) + 
               (attendanceScore * 0.1) + (finalScore * 0.5);
    }
    
    /**
     * Gets the letter grade (A, B, C, D, F) based on the average score.
     * 
     * @return the letter grade
     */
    public String getLetterGrade() {
        double average = calculateAverage();
        
        if (average >= 8.5) return "A";
        if (average >= 7.0) return "B";
        if (average >= 5.5) return "C";
        if (average >= 4.0) return "D";
        return "F";
    }
    
    // Getters and setters
    public Student getStudent() {
        return student;
    }
    
    public Subject getSubject() {
        return subject;
    }
    
    public double getAssignmentScore() {
        return assignmentScore;
    }
    
    public void setAssignmentScore(double assignmentScore) {
        this.assignmentScore = assignmentScore;
    }
    
    public double getMidtermScore() {
        return midtermScore;
    }
    
    public void setMidtermScore(double midtermScore) {
        this.midtermScore = midtermScore;
    }
    
    public double getAttendanceScore() {
        return attendanceScore;
    }
    
    public void setAttendanceScore(double attendanceScore) {
        this.attendanceScore = attendanceScore;
    }
    
    public double getFinalScore() {
        return finalScore;
    }
    
    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }
}