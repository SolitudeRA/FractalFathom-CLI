package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model;

import org.protogalaxy.fractalfathom.*;

@FractalFathomFeature(name = "UserManagement", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "user management")
public class User {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}