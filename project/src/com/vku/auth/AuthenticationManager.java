package com.vku.auth;

import com.vku.data.DataManager;
import com.vku.model.User;

/**
 * Handles user authentication for the application.
 */
public class AuthenticationManager {
    private static User currentUser;
    
    /**
     * Authenticates a user based on role, email and password.
     * 
     * @param role the user's role (Teacher or Student)
     * @param email the user's email
     * @param password the user's password
     * @return true if authentication is successful, false otherwise
     */
    public static boolean authenticate(String role, String email, String password) {
        for (User user : DataManager.getInstance().getUsers()) {
            if (user.getEmail().equals(email) && 
                user.getPassword().equals(password) && 
                user.getClass().getSimpleName().equalsIgnoreCase(role)) {
                
                currentUser = user;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the currently authenticated user.
     * 
     * @return the current user, or null if no user is authenticated
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Logs out the current user.
     */
    public static void logout() {
        currentUser = null;
    }
    
    /**
     * Checks if the current user is a teacher.
     * 
     * @return true if the current user is a teacher, false otherwise
     */
    public static boolean isTeacher() {
        return currentUser != null && currentUser.getClass().getSimpleName().equalsIgnoreCase("Teacher");
    }
    
    /**
     * Checks if the current user is a student.
     * 
     * @return true if the current user is a student, false otherwise
     */
    public static boolean isStudent() {
        return currentUser != null && currentUser.getClass().getSimpleName().equalsIgnoreCase("Student");
    }
}