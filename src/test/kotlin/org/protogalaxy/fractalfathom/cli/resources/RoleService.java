package org.protogalaxy.fractalfathom.cli.resources;

import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;

@FractalFathomFeature(name = "RoleService", description = "Manages user roles", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(toConcept = "Role Management", type = MappingType.MODULE)
public class RoleService {

    @FractalFathomMapping(toConcept = "Role Repository", type = MappingType.COMPONENT)
    private RoleRepository roleRepository;

    @FractalFathomMapping(toConcept = "Permission Service Interaction", type = MappingType.COMPONENT)
    private PermissionService permissionService;

    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    @FractalFathomMapping(toConcept = "Role Assignment", type = MappingType.COMPONENT)
    public void assignRole(String username, String role) {
        // Assign a role to the user
        roleRepository.assignRole(username, role);

        // Automatically grant default permissions for the role
        permissionService.grantDefaultPermissions(role, username);
    }

    @FractalFathomMapping(toConcept = "Revoke All Roles", type = MappingType.COMPONENT)
    public void revokeAllRoles(String username) {
        // Revoke all roles for a user
        roleRepository.revokeAllRoles(username);

        // Revoke all permissions for the user
        permissionService.revokeAllPermissions(username);
    }
}