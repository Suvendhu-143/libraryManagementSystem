package com.library.patterns;

import com.library.utils.Logger;

/**
 * Concrete observer implementation for email notifications.
 */
public class EmailNotificationObserver implements Observer {
    private String email;

    public EmailNotificationObserver(String email) {
        this.email = email;
    }

    @Override
    public void update(String message) {
        Logger.log("Sending email to " + email + ": " + message);
        // In a real implementation, this would send an actual email
    }

    public String getEmail() {
        return email;
    }
}
