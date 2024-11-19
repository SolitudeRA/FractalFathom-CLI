package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "PermissionService", description = "Handles user permissions", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(toConcept = "Permission Management", type = MappingType.MODULE)
public class PermissionService {

    @FractalFathomMapping(toConcept = "Permission Repository", type = MappingType.COMPONENT)
    private PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @FractalFathomMapping(toConcept = "Grant Default Permissions", type = MappingType.COMPONENT)
    public void grantDefaultPermissions(String role, String username) {
        // Grant default permissions based on the role
        permissionRepository.grantDefaultPermissions(role, username);
    }

    @FractalFathomMapping(toConcept = "Revoke All Permissions", type = MappingType.COMPONENT)
    public void revokeAllPermissions(String username) {
        // Revoke all permissions for a user
        permissionRepository.revokeAll(username);
    }
}
