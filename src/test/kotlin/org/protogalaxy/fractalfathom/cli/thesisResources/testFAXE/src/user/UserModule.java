package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXE.src.user;

import java.util.ArrayList;
import java.util.List;

//&begin [UserManagement]
public class UserModule {

    //&begin [UserStorage]
    private final List<User> users = new ArrayList<>();
    //&end [UserStorage]

    //&begin [AddUser]
    public void addUser(User user) {
        users.add(user);
    }
    //&end [AddUser]

    //&begin [RemoveUser]
    public boolean removeUser(String userId) {
        return users.removeIf(user -> user.getId().equals(userId));
    }
    //&end [RemoveUser]

    //&begin [FindUserByID]
    public User findUserById(String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    //&end [FindUserByID]
}
//&end [UserManagement]
