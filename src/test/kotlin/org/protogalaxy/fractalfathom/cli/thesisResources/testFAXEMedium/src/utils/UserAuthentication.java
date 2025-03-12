package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.utils;

import org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.model.User;

//&begin [UserAuthentication]
public class UserAuthentication {
    //&begin [login]
    public User login(int userId, String password) {
        if (userId == 1 && "password123".equals(password)) {
            return new User(userId, "Alice", "alice@example.com");
        }
        System.out.println("Invalid credentials for user ID: " + userId);
        return null;
    }
    //&end [login]
}
//&end [UserAuthentication]