package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXE.src.auth;

import user.UserModule;

//&begin [Authentication]
public class AuthenticationModule {
    private final UserModule userModule;

    //&begin [AuthenticationConstructor]
    public AuthenticationModule(UserModule userModule) {
        this.userModule = userModule;
    }
    //&end [AuthenticationConstructor]

    //&begin [AuthenticateUser]
    public boolean authenticate(String userId) {
        return userModule.findUserById(userId) != null;
    }
    //&end [AuthenticateUser]
}
//&end [Authentication]
