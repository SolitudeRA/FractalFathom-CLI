package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXE.src.user;

//&begin [UserEntity]
public class User {

    //&line [UserID]
    private final String id;

    //&line [UserName]
    private final String name;

    //&begin [UserConstructor]
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    //&end [UserConstructor]

    //&begin [GetUserID]
    public String getId() {
        return id;
    }
    //&end [GetUserID]

    //&begin [GetUserName]
    public String getName() {
        return name;
    }
    //&end [GetUserName]
}
//&end [UserEntity]
