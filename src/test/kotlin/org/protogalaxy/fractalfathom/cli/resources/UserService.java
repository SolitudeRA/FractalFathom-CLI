package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.cli.resources.annotations.Feature;
import org.protogalaxy.fractalfathom.cli.resources.annotations.FeatureType;
import org.protogalaxy.fractalfathom.cli.resources.annotations.Mapping;
import org.protogalaxy.fractalfathom.cli.resources.annotations.MappingType;

@Feature(name = "UserService", description = "Handles user-related operations", type = FeatureType.FUNCTIONAL)
@Mapping(toConcept = "User Management", type = MappingType.MODULE)
public class UserService {

    @Feature(name = "userRepository", description = "Repository for accessing user data", type = FeatureType.NON_FUNCTIONAL)
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Feature(name = "createUser", description = "Creates a new user", type = FeatureType.FUNCTIONAL)
    @Mapping(toConcept = "User Creation", type = MappingType.COMPONENT)
    public void createUser(String username, String email) {
        // Logic to create a new user
        userRepository.save(new User(username, email));
    }

    @Feature(name = "deleteUser", description = "Deletes an existing user", type = FeatureType.FUNCTIONAL)
    @Mapping(toConcept = "User Deletion", type = MappingType.COMPONENT)
    public void deleteUser(String userId) {
        // Logic to delete a user
        userRepository.delete(userId);
    }
}

