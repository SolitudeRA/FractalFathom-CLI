package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "PermissionRepository", description = "Provides CRUD operations for permission data", type = FeatureType.NON_FUNCTIONAL)
@FractalFathomMapping(toConcept = "Permission Data Access", type = MappingType.COMPONENT)
public class PermissionRepository {
    public void grantDefaultPermissions(String role, String username) {
        // Grant default permissions for the role
    }

    public void revokeAll(String username) {
        // Revoke all permissions for a user
    }
}

