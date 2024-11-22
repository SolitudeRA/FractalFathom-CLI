package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@FractalFathomFeature(name = "UserRepository", description = "Provides CRUD operations for user data", type = FeatureType.NON_FUNCTIONAL)
@FractalFathomMapping(toConcept = "User Data Access", type = MappingType.COMPONENT)
public class UserRepository {

    private final Map<String, User> database = new HashMap<>();

    /**
     * Saves a user to the in-memory database.
     *
     * @param user the user to save
     */
    @FractalFathomMapping(toConcept = "User Management", type = MappingType.MODULE)
    public void save(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("User or username cannot be null or empty.");
        }
        database.put(user.getUsername(), user);
    }

    /**
     * Deletes a user from the in-memory database by username.
     *
     * @param username the username of the user to delete
     */
    public void delete(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (!database.containsKey(username)) {
            throw new IllegalStateException("User with username '" + username + "' does not exist.");
        }
        database.remove(username);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the user if found, or an empty Optional if not
     */
    @FractalFathomMapping(toConcept = "User Retrieval", type = MappingType.COMPONENT)
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        return Optional.ofNullable(database.get(username));
    }
}


