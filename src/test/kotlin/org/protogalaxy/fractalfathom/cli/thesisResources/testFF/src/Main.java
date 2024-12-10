package org.protogalaxy.fractalfathom.cli.thesisResources.testFF.src;

import auth.AuthenticationModule;
import permission.PermissionModule;
import user.User;
import user.UserModule;

public class Main {
    public static void main(String[] args) {
        UserModule userModule = new UserModule();
        PermissionModule permissionModule = new PermissionModule();
        AuthenticationModule authModule = new AuthenticationModule(userModule);

        // Add users
        userModule.addUser(new User("1", "Alice"));
        userModule.addUser(new User("2", "Bob"));

        // Grant permissions
        permissionModule.grantPermission("1", "ADMIN");
        permissionModule.grantPermission("2", "USER");

        // Authenticate users
        System.out.println("Authentication for Alice: " + authModule.authenticate("1"));
        System.out.println("Authentication for Bob: " + authModule.authenticate("2"));

        // Check permissions
        System.out.println("Alice has ADMIN permission: " +
                permissionModule.checkPermission("1", "ADMIN"));
        System.out.println("Bob has ADMIN permission: " +
                permissionModule.checkPermission("2", "ADMIN"));
    }
}