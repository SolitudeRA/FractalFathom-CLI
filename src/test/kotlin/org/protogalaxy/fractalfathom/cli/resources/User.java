package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "User", description = "Represents a user in the system", type = FeatureType.NON_FUNCTIONAL)
@FractalFathomMapping(toConcept = "User Entity", type = MappingType.COMPONENT)
public class User {

    @FractalFathomMapping(toConcept = "Username", type = MappingType.COMPONENT)
    private String username;

    @FractalFathomMapping(toConcept = "Email Address", type = MappingType.COMPONENT)
    private String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @FractalFathomMapping(toConcept = "Get Username", type = MappingType.COMPONENT)
    public String getUsername() {
        return username;
    }

    @FractalFathomMapping(toConcept = "Set Username", type = MappingType.COMPONENT)
    public void setUsername(String username) {
        this.username = username;
    }

    @FractalFathomMapping(toConcept = "Get Email Address", type = MappingType.COMPONENT)
    public String getEmail() {
        return email;
    }

    @FractalFathomMapping(toConcept = "Set Email Address", type = MappingType.COMPONENT)
    public void setEmail(String email) {
        this.email = email;
    }
}

