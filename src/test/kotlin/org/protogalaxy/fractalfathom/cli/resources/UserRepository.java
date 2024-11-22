package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "UserRepository", description = "Provides CRUD operations for user data", type = FeatureType.NON_FUNCTIONAL)
@FractalFathomMapping(toConcept = "User Data Access", type = MappingType.COMPONENT)
public class UserRepository {

    @FractalFathomMapping(toConcept = "User Management", type = MappingType.MODULE)
    public void save(User user) {
        // Save user to database
        String username = user.getUsername();
    }

    public void delete(String username) {
        // Delete user from database
    }
}


