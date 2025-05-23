package com.vku.data;

import com.vku.model.*;

import java.util.*;

/**
 * DataManager handles all in-memory data storage for the application.
 * Implements the Singleton pattern to ensure a single instance.
 */
public class DataManager {
    private static DataManager instance;
    
    // Collections to store application data
    private List<User> users;
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Subject> subjects;
    private List<Project> projects;
    private List<Attendance> attendanceRecords;
    private List<Fee> fees;
    private List<Notification> notifications;
    
    private DataManager() {
        // Initialize collections
        users = new ArrayList<>();
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        subjects = new ArrayList<>();
        projects = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        fees = new ArrayList<>();
        notifications = new ArrayList<>();
    }
    
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    /**
     * Initialize sample data for testing
     */
    public void initializeData() {
        // Create teachers
        Teacher teacher1 = new Teacher("T001", "Nguyen Van Teacher", "teacher1@vku.vn", "teacher123");
        Teacher teacher2 = new Teacher("T002", "Tran Thi Lecturer", "teacher2@vku.vn", "teacher123");
        
        teachers.add(teacher1);
        teachers.add(teacher2);
        users.add(teacher1);
        users.add(teacher2);
        
        // Create students
        Student student1 = new Student("S001", "Nguyen Van A", "K41A", "Information Technology", "2021-2025", "student1@vku.vn", "student123");
        Student student2 = new Student("S002", "Tran Thi B", "K41A", "Information Technology", "2021-2025", "student2@vku.vn", "student123");
        Student student3 = new Student("S003", "Le Van C", "K42B", "Business Administration", "2022-2026", "student3@vku.vn", "student123");
        Student student4 = new Student("S004", "Pham Thi D", "K40C", "Design", "2020-2024", "student4@vku.vn", "student123");
        Student student5 = new Student("S005", "Hoang Van E", "K42B", "Business Administration", "2022-2026", "student5@vku.vn", "student123");
        
        // Set additional student info
        student1.setDateOfBirth("2003-05-15");
        student1.setGender("Male");
        student1.setParentName("Nguyen Van Parent");
        student1.setParentContact("0123456789");
        student1.setAddress("123 ABC Street, Da Nang");
        student1.setPhone("0987654321");
        
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        users.add(student1);
        users.add(student2);
        users.add(student3);
        users.add(student4);
        users.add(student5);
        
        // Create subjects
        Subject subject1 = new Subject("SUB001", "Java Programming", 3, teacher1);
        Subject subject2 = new Subject("SUB002", "Web Development", 3, teacher1);
        Subject subject3 = new Subject("SUB003", "Business Management", 2, teacher2);
        
        subjects.add(subject1);
        subjects.add(subject2);
        subjects.add(subject3);
        
        // Assign subjects to students and create grades
        assignSubjectToStudent(student1, subject1, 8.5, 8.0, 9.0, 8.2);
        assignSubjectToStudent(student1, subject2, 7.5, 8.5, 8.0, 7.9);
        assignSubjectToStudent(student2, subject1, 7.0, 6.5, 7.5, 7.2);
        assignSubjectToStudent(student2, subject2, 8.0, 7.5, 8.5, 8.1);
        assignSubjectToStudent(student3, subject3, 9.0, 8.5, 9.5, 9.2);
        assignSubjectToStudent(student4, subject3, 8.5, 8.0, 8.0, 8.1);
        assignSubjectToStudent(student5, subject3, 7.5, 7.0, 7.0, 7.1);
        
        // Create projects
        List<Student> projectGroup1 = new ArrayList<>();
        projectGroup1.add(student1);
        projectGroup1.add(student2);
        
        Project project1 = new Project("P001", "Student Management System", projectGroup1, teacher1, "2023-12-31");
        project1.setProgress(75);
        project1.setGrade(8.5);
        
        List<Student> projectGroup2 = new ArrayList<>();
        projectGroup2.add(student3);
        projectGroup2.add(student4);
        projectGroup2.add(student5);
        
        Project project2 = new Project("P002", "Business Analysis Tool", projectGroup2, teacher2, "2023-12-15");
        project2.setProgress(60);
        project2.setGrade(7.8);
        
        projects.add(project1);
        projects.add(project2);
        
        // Create attendance records
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        
        for (Student student : students) {
            for (Subject subject : subjects) {
                if (student.getSubjects().contains(subject)) {
                    Attendance attendance = new Attendance(student, subject, today, true);
                    attendanceRecords.add(attendance);
                }
            }
        }
        
        // Create fees
        for (Student student : students) {
            double feeAmount = student.getSubjects().size() * 1000000;
            Fee fee = new Fee(student, feeAmount, student.getId().equals("S001") || student.getId().equals("S003"));
            fees.add(fee);
        }
        
        // Create notifications
        Notification notification1 = new Notification("System", "Welcome to VKU Student Management System!", new Date(), null);
        Notification notification2 = new Notification(teacher1.getName(), "Java Programming final project submission deadline is approaching.", new Date(), subject1);
        
        notifications.add(notification1);
        notifications.add(notification2);
    }
    
    private void assignSubjectToStudent(Student student, Subject subject, double assignment, double midterm, double attendance, double finalGrade) {
        student.addSubject(subject);
        
        // Create grade for this subject
        Grade grade = new Grade(student, subject);
        grade.setAssignmentScore(assignment);
        grade.setMidtermScore(midterm);
        grade.setAttendanceScore(attendance);
        grade.setFinalScore(finalGrade);
        
        student.addGrade(grade);
    }
    
    // Getters for collections
    public List<User> getUsers() {
        return users;
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public List<Teacher> getTeachers() {
        return teachers;
    }
    
    public List<Subject> getSubjects() {
        return subjects;
    }
    
    public List<Project> getProjects() {
        return projects;
    }
    
    public List<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }
    
    public List<Fee> getFees() {
        return fees;
    }
    
    public List<Notification> getNotifications() {
        return notifications;
    }
}