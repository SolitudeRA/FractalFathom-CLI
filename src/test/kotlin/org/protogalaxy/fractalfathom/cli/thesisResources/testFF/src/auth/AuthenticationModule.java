package org.protogalaxy.fractalfathom.cli.thesisResources.testFF.src.auth;

import org.protogalaxy.fractalfathom.FractalFathomFeature;
import org.protogalaxy.fractalfathom.FractalFathomMapping;
import org.protogalaxy.fractalfathom.FeatureType;
import org.protogalaxy.fractalfathom.MappingType;
import user.UserModule;

@FractalFathomFeature(
        name = "Authentication Module",
        description = "Handles user authentication.",
        type = FeatureType.FUNCTIONAL
)
@FractalFathomMapping(
        toConcept = "UserAuthentication",
        type = MappingType.COMPONENT
)
public class AuthenticationModule {
    private final UserModule userModule;

    public AuthenticationModule(UserModule userModule) {
        this.userModule = userModule;
    }

    public boolean authenticate(String userId) {
        return userModule.findUserById(userId) != null;
    }
}
