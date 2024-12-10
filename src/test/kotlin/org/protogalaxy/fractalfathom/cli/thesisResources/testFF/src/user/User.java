package org.protogalaxy.fractalfathom.cli.thesisResources.testFF.src.user;

import org.protogalaxy.fractalfathom.FractalFathomMapping;
import org.protogalaxy.fractalfathom.MappingType;

@FractalFathomMapping(
        toConcept = "User Entity",
        type = MappingType.DATA
)
public class User {

    private final String id;

    private final String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}