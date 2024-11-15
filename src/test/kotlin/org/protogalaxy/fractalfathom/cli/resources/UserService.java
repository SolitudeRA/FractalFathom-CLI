package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;
import org.protogalaxy.fractalfathom.MappingType;

@FractalFathomFeature(name = "UserService", description = "Handles user-related operations", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(toConcept = "User Management", type = MappingType.MODULE)
public class UserService {

    @FractalFathomFeature(name = "userRepository", description = "Repository for accessing user data", type = FeatureType.NON_FUNCTIONAL)
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @FractalFathomFeature(name = "createUser", description = "Creates a new user", type = FeatureType.FUNCTIONAL)
    @FractalFathomMapping(toConcept = "User Creation", type = MappingType.COMPONENT)
    public void createUser(String username, String email) {
        // Logic to create a new user
        userRepository.save(new User(username, email));
    }

    @FractalFathomFeature(name = "deleteUser", description = "Deletes an existing user", type = FeatureType.FUNCTIONAL)
    @FractalFathomMapping(toConcept = "User Deletion", type = MappingType.COMPONENT)
    public void deleteUser(String userId) {
        // Logic to delete a user
        userRepository.delete(userId);
    }
}

