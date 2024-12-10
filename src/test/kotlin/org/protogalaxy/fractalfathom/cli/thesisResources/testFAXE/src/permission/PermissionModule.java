package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXE.src.permission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//&begin [PermissionManagement]
public class PermissionModule {

    //&begin [PermissionStorage]
    private final Map<String, Set<String>> userPermissions = new HashMap<>();
    //&end [PermissionStorage]

    //&begin [GrantPermission]
    public void grantPermission(String userId, String permission) {
        userPermissions.computeIfAbsent(userId, k -> new HashSet<>()).add(permission);
    }
    //&end [GrantPermission]

    //&begin [RevokePermission]
    public void revokePermission(String userId, String permission) {
        Set<String> permissions = userPermissions.get(userId);
        if (permissions != null) {
            permissions.remove(permission);
        }
    }
    //&end [RevokePermission]

    //&begin [CheckPermission]
    public boolean checkPermission(String userId, String permission) {
        return userPermissions.getOrDefault(userId, Set.of()).contains(permission);
    }
    //&end [CheckPermission]
}
//&end [PermissionManagement]
