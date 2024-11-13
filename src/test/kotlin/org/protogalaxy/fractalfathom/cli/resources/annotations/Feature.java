package org.protogalaxy.fractalfathom.cli.resources.annotations;

public @interface Feature {
    String name();
    String description() default "";
    FeatureType type();
}