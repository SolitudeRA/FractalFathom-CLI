package org.protogalaxy.fractalfathom.cli.thesisResources.testFF.src.permission;

import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;
import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@FractalFathomFeature(
        name = "Permission Management Module",
        description = "Manages user permissions such as granting, revoking, and checking permissions.",
        type = FeatureType.FUNCTIONAL
)
@FractalFathomMapping(
        toConcept = "PermissionManagement",
        type = MappingType.MODULE
)
public class PermissionModule {

    private final Map<String, Set<String>> userPermissions = new HashMap<>();

    public void grantPermission(String userId, String permission) {
        userPermissions.computeIfAbsent(userId, k -> new HashSet<>()).add(permission);
    }

    public void revokePermission(String userId, String permission) {
        Set<String> permissions = userPermissions.get(userId);
        if (permissions != null) {
            permissions.remove(permission);
        }
    }

    public boolean checkPermission(String userId, String permission) {
        return userPermissions.getOrDefault(userId, Set.of()).contains(permission);
    }
}