package org.protogalaxy.fractalfathom.cli.thesisResources.testFF.src.user;

import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;
import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;

import java.util.ArrayList;
import java.util.List;

@FractalFathomFeature(
        name = "User Management Module",
        description = "Handles core user management operations.",
        type = FeatureType.FUNCTIONAL
)
@FractalFathomMapping(
        toConcept = "UserManagement",
        type = MappingType.MODULE
)
public class UserModule {

    private final List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public boolean removeUser(String userId) {
        return users.removeIf(user -> user.getId().equals(userId));
    }

    public User findUserById(String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
