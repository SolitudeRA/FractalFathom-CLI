package org.protogalaxy.fractalfathom;

public @interface FractalFathomMapping {
    String toConcept();
    MappingType type() default MappingType.CONCEPT;
}
