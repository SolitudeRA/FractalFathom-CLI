package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXE.src;

import se.gu.faxe.Asset;
import se.gu.faxe.FAXE;

import user.User;
import user.UserModule;
import permission.PermissionModule;
import auth.AuthenticationModule;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            UserModule userModule = new UserModule();
            PermissionModule permissionModule = new PermissionModule();
            AuthenticationModule authModule = new AuthenticationModule(userModule);

            userModule.addUser(new User("1", "Alice"));
            userModule.addUser(new User("2", "Bob"));

            permissionModule.grantPermission("1", "ADMIN");
            permissionModule.grantPermission("2", "USER");

            System.out.println("Authentication for Alice: " + authModule.authenticate("1"));
            System.out.println("Authentication for Bob: " + authModule.authenticate("2"));

            System.out.println("Exporting embedded annotation feature model...");

            File projectRoot = new File("D:/Codeing/testProject/testProjectFAXE/src");
            FAXE faxe = new FAXE(projectRoot);
            File pathFeatureModel = new File("D:/Codeing/testProject/testProjectFAXE/src/.feature-model");

            faxe.getEmbeddedAnnotationsFeatureModel(new Asset(pathFeatureModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
