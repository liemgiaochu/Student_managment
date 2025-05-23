package com.vku.model;

import java.util.Date;

/**
 * Represents a notification or announcement in the system.
 */
public class Notification {
    private String sender;
    private String message;
    private Date date;
    private Subject relatedSubject; // Can be null for general announcements
    
    public Notification(String sender, String message, Date date, Subject relatedSubject) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.relatedSubject = relatedSubject;
    }
    
    // Getters and setters
    public String getSender() {
        return sender;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Date getDate() {
        return date;
    }
    
    public Subject getRelatedSubject() {
        return relatedSubject;
    }
}