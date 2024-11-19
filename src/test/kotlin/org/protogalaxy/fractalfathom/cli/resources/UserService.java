package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "UserService", description = "Handles user-related operations", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(toConcept = "User Management", type = MappingType.MODULE)
public class UserService {

    @FractalFathomMapping(toConcept = "User Repository", type = MappingType.COMPONENT)
    private UserRepository userRepository;

    @FractalFathomMapping(toConcept = "Role Service Interaction", type = MappingType.COMPONENT)
    private RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @FractalFathomMapping(toConcept = "User Creation", type = MappingType.COMPONENT)
    public void createUser(String username, String email, String role) {
        // Create a new user
        userRepository.save(new User(username, email));

        // Assign a default role to the new user
        roleService.assignRole(username, role);
    }

    @FractalFathomMapping(toConcept = "User Deletion", type = MappingType.COMPONENT)
    public void deleteUser(String username) {
        // Delete the user
        userRepository.delete(username);

        // Revoke all roles for the deleted user
        roleService.revokeAllRoles(username);
    }
}
