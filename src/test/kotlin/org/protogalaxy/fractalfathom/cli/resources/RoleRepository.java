package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "RoleRepository", description = "Provides CRUD operations for role data", type = FeatureType.NON_FUNCTIONAL)
@FractalFathomMapping(toConcept = "Role Data Access", type = MappingType.COMPONENT)
public class RoleRepository {
    public void assignRole(String username, String role) {
        // Assign role to user
    }

    public void revokeAllRoles(String username) {
        // Revoke all roles for user
    }
}


