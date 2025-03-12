package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.utils;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.User;

@FractalFathomFeature(name = "UserAuthentication", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "utils")
public class UserAuthentication {
    public User login(int userId, String password) {
        if (userId == 1 && "password123".equals(password)) {
            return new User(userId, "Alice", "alice@example.com");
        }
        System.out.println("Invalid credentials for user ID: " + userId);
        return null;
    }
}