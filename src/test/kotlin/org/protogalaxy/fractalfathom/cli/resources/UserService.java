package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

import java.util.Optional;

@FractalFathomFeature(name = "UserService", description = "Handles user-related operations", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(toConcept = "User Management", type = MappingType.MODULE)
public class UserService {

    @FractalFathomMapping(toConcept = "User Repository", type = MappingType.COMPONENT)
    private final UserRepository userRepository;

    @FractalFathomMapping(toConcept = "Role Service Interaction", type = MappingType.COMPONENT)
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    /**
     * Creates a new user with the specified details and assigns them a default role.
     *
     * @param username the username of the new user
     * @param email    the email of the new user
     * @param role     the default role to assign to the user
     */
    @FractalFathomMapping(toConcept = "User Creation", type = MappingType.COMPONENT)
    public void createUser(String username, String email, String role) {
        // Validate input parameters
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalStateException("User with username '" + username + "' already exists.");
        }

        // Save the new user
        User newUser = new User(username, email);
        userRepository.save(newUser);

        // Assign the default role to the new user
        roleService.assignRole(username, role);
    }

    /**
     * Deletes an existing user and revokes all associated roles.
     *
     * @param username the username of the user to delete
     */
    @FractalFathomMapping(toConcept = "User Deletion", type = MappingType.COMPONENT)
    public void deleteUser(String username) {
        // Validate input parameter
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        // Check if the user exists
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalStateException("User with username '" + username + "' does not exist.");
        }

        // Delete the user
        userRepository.delete(username);

        // Revoke all roles for the deleted user
        roleService.revokeAllRoles(username);
    }

    /**
     * Updates the email address of an existing user.
     *
     * @param username the username of the user to update
     * @param newEmail the new email address
     */
    @FractalFathomMapping(toConcept = "User Update", type = MappingType.COMPONENT)
    public void updateUserEmail(String username, String newEmail) {
        // Validate input parameters
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (newEmail == null || newEmail.isEmpty()) {
            throw new IllegalArgumentException("New email cannot be null or empty.");
        }

        // Check if the user exists
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalStateException("User with username '" + username + "' does not exist.");
        }

        // Update the user's email
        User existingUser = user.get();
        existingUser.setEmail(newEmail);
        userRepository.save(existingUser);
    }
}
